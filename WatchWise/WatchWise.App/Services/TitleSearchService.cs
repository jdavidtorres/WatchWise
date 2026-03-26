using System.Net;
using System.Net.Http;
using System.Text.Json;
using System.Text.Json.Serialization;
using WatchWise.App.Models;

namespace WatchWise.App.Services;

public class ApiClientException : Exception
{
    public HttpStatusCode? StatusCode { get; }

    public ApiClientException(string message, HttpStatusCode? statusCode = null) : base(message)
    {
        StatusCode = statusCode;
    }

    public ApiClientException(string message, Exception innerException) : base(message, innerException)
    {
    }
}

public class ApiClient : IApiClient
{
    private readonly HttpClient _httpClient;
    private readonly JsonSerializerOptions _jsonOptions;

    public ApiClient(string baseUrl = "http://localhost:8080")
        : this(new HttpClient
        {
            BaseAddress = new Uri(baseUrl),
            Timeout = TimeSpan.FromSeconds(30)
        })
    {
    }

    public ApiClient(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true,
            Converters = { new JsonStringEnumConverter() }
        };
    }

    public async Task<List<TitleLite>> SearchTitlesAsync(string query, TitleType? type = null, int page = 0, CancellationToken cancellationToken = default)
    {
        if (string.IsNullOrWhiteSpace(query) || query.Length < 2)
            return new List<TitleLite>();

        var url = $"/api/titles/search?q={Uri.EscapeDataString(query)}&page={page}";
        if (type.HasValue)
            url += $"&type={type.Value}";

        try
        {
            var response = await _httpClient.GetAsync(url, cancellationToken);
            await EnsureSuccessAsync(response, cancellationToken);

            var json = await response.Content.ReadAsStringAsync(cancellationToken);
            return JsonSerializer.Deserialize<List<TitleLite>>(json, _jsonOptions) ?? new List<TitleLite>();
        }
        catch (ApiClientException)
        {
            throw;
        }
        catch (TaskCanceledException ex) when (!cancellationToken.IsCancellationRequested)
        {
            throw new ApiClientException("The request timed out. Please check your connection and try again.", ex);
        }
        catch (HttpRequestException ex)
        {
            throw new ApiClientException($"Network error while searching titles: {ex.Message}", ex);
        }
    }

    public async Task<TitleDetail?> GetTitleDetailsAsync(string? id, CancellationToken cancellationToken = default)
    {
        if (string.IsNullOrWhiteSpace(id))
            return null;

        try
        {
            var response = await _httpClient.GetAsync($"/api/titles/{Uri.EscapeDataString(id)}", cancellationToken);

            if (response.StatusCode == HttpStatusCode.NotFound)
                return null;

            await EnsureSuccessAsync(response, cancellationToken);

            var json = await response.Content.ReadAsStringAsync(cancellationToken);
            return JsonSerializer.Deserialize<TitleDetail>(json, _jsonOptions);
        }
        catch (ApiClientException)
        {
            throw;
        }
        catch (TaskCanceledException ex) when (!cancellationToken.IsCancellationRequested)
        {
            throw new ApiClientException("The request timed out. Please check your connection and try again.", ex);
        }
        catch (HttpRequestException ex)
        {
            throw new ApiClientException($"Network error while getting title details: {ex.Message}", ex);
        }
    }

    private static async Task EnsureSuccessAsync(HttpResponseMessage response, CancellationToken cancellationToken)
    {
        if (response.IsSuccessStatusCode)
            return;

        var body = await response.Content.ReadAsStringAsync(cancellationToken);
        var message = (int)response.StatusCode switch
        {
            >= 400 and < 500 => $"Client error {(int)response.StatusCode}: {body}",
            >= 500 => $"Server error {(int)response.StatusCode}: the service is unavailable, please try again later.",
            _ => $"Unexpected response {(int)response.StatusCode}: {body}"
        };

        throw new ApiClientException(message, response.StatusCode);
    }
}
