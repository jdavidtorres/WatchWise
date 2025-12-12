using System.Net.Http;
using System.Text.Json;
using WatchWise.App.Models;

namespace WatchWise.App.Services;

public class ApiClient : IApiClient
{
    private readonly HttpClient _httpClient;
    private readonly JsonSerializerOptions _jsonOptions;

    public ApiClient()
    {
        _httpClient = new HttpClient { BaseAddress = new Uri("http://localhost:8080") };
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        };
    }

    public async Task<List<TitleLite>> SearchTitlesAsync(string query, TitleType? type = null, int page = 0, CancellationToken cancellationToken = default)
    {
        if (string.IsNullOrWhiteSpace(query) || query.Length < 2)
            return new List<TitleLite>();

        var url = $"/api/titles/search?q={Uri.EscapeDataString(query)}&page={page}";
        if (type.HasValue)
        {
            url += $"&type={type.Value}";
        }

        try
        {
            var response = await _httpClient.GetAsync(url, cancellationToken);
            response.EnsureSuccessStatusCode();
            
            var json = await response.Content.ReadAsStringAsync(cancellationToken);
            return JsonSerializer.Deserialize<List<TitleLite>>(json, _jsonOptions) ?? new List<TitleLite>();
        }
        catch (Exception ex)
        {
            // Log error in a real app
            System.Diagnostics.Debug.WriteLine($"Error searching titles: {ex.Message}");
            return new List<TitleLite>();
        }
    }

    public async Task<TitleDetail?> GetTitleDetailsAsync(string id, CancellationToken cancellationToken = default)
    {
        if (string.IsNullOrWhiteSpace(id))
            return null;

        try
        {
            var response = await _httpClient.GetAsync($"/api/titles/{Uri.EscapeDataString(id)}", cancellationToken);
            if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                return null;
                
            response.EnsureSuccessStatusCode();
            
            var json = await response.Content.ReadAsStringAsync(cancellationToken);
            return JsonSerializer.Deserialize<TitleDetail>(json, _jsonOptions);
        }
        catch (Exception ex)
        {
            // Log error in a real app
            System.Diagnostics.Debug.WriteLine($"Error getting title details: {ex.Message}");
            return null;
        }
    }
}
