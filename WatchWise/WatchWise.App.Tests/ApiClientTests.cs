using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using WatchWise.App.Models;
using WatchWise.App.Services;
using Xunit;

namespace WatchWise.App.Tests;

public class ApiClientTests
{
    private static ApiClient CreateApiClient(HttpStatusCode statusCode, string responseBody)
    {
        var handler = new FakeHttpMessageHandler(statusCode, responseBody);
        var httpClient = new HttpClient(handler) { BaseAddress = new Uri("http://localhost:8080") };
        return new ApiClient(httpClient);
    }

    // ─── SearchTitlesAsync ─────────────────────────────────────────────────────

    [Fact]
    public async Task SearchTitlesAsync_ValidQuery_DeserializesResults()
    {
        var titles = new[]
        {
            new { id = "tt0133093", type = "MOVIE", name = "The Matrix", year = 1999, posterUrl = "https://example.com/poster.jpg" }
        };
        var json = JsonSerializer.Serialize(titles);
        var apiClient = CreateApiClient(HttpStatusCode.OK, json);

        var results = await apiClient.SearchTitlesAsync("matrix");

        Assert.Single(results);
        Assert.Equal("tt0133093", results[0].Id);
        Assert.Equal(TitleType.MOVIE, results[0].Type);
        Assert.Equal("The Matrix", results[0].Name);
        Assert.Equal(1999, results[0].Year);
        Assert.Equal("https://example.com/poster.jpg", results[0].PosterUrl);
    }

    [Fact]
    public async Task SearchTitlesAsync_EmptyArray_ReturnsEmptyList()
    {
        var apiClient = CreateApiClient(HttpStatusCode.OK, "[]");

        var results = await apiClient.SearchTitlesAsync("noresults");

        Assert.Empty(results);
    }

    [Theory]
    [InlineData("")]
    [InlineData("  ")]
    [InlineData("a")]
    public async Task SearchTitlesAsync_ShortOrEmptyQuery_ReturnsEmptyWithoutRequest(string query)
    {
        var handler = new CountingHttpMessageHandler();
        var httpClient = new HttpClient(handler) { BaseAddress = new Uri("http://localhost:8080") };
        var apiClient = new ApiClient(httpClient);

        var results = await apiClient.SearchTitlesAsync(query);

        Assert.Empty(results);
        Assert.Equal(0, handler.RequestCount);
    }

    [Fact]
    public async Task SearchTitlesAsync_ServerError_ThrowsApiClientException()
    {
        var apiClient = CreateApiClient(HttpStatusCode.InternalServerError, "Internal Server Error");
    }

    [Fact]
    public async Task SearchTitlesAsync_ClientError_ThrowsApiClientException()
    {
        var apiClient = CreateApiClient(HttpStatusCode.BadRequest, "Bad Request");

        var ex = await Assert.ThrowsAsync<ApiClientException>(() => apiClient.SearchTitlesAsync("matrix"));

        Assert.Equal(HttpStatusCode.BadRequest, ex.StatusCode);
        Assert.Contains("400", ex.Message);
    }

    [Fact]
    public async Task SearchTitlesAsync_Timeout_ThrowsApiClientExceptionWithTimeoutMessage()
    {
        var handler = new TimeoutHttpMessageHandler();
        var httpClient = new HttpClient(handler) { BaseAddress = new Uri("http://localhost:8080") };
        var apiClient = new ApiClient(httpClient);

        var ex = await Assert.ThrowsAsync<ApiClientException>(() => apiClient.SearchTitlesAsync("matrix"));

        Assert.Contains("timed out", ex.Message, StringComparison.OrdinalIgnoreCase);
    }

    [Fact]
    public async Task SearchTitlesAsync_CancellationRequested_ThrowsTaskCancelledException()
    {
        var handler = new DelayedHttpMessageHandler();
        var httpClient = new HttpClient(handler) { BaseAddress = new Uri("http://localhost:8080") };
        var apiClient = new ApiClient(httpClient);
        using var cts = new CancellationTokenSource();
        cts.Cancel();

        await Assert.ThrowsAsync<TaskCanceledException>(() =>
            apiClient.SearchTitlesAsync("matrix", cancellationToken: cts.Token));
    }

    // ─── GetTitleDetailsAsync ──────────────────────────────────────────────────

    [Fact]
    public async Task GetTitleDetailsAsync_ValidId_DeserializesDetail()
    {
        var detail = new
        {
            id = "tt0133093",
            type = "MOVIE",
            name = "The Matrix",
            year = 1999,
            overview = "A computer hacker learns the truth.",
            runtime = 136,
            genres = new[] { "Action", "Sci-Fi" },
            posterUrl = "https://example.com/poster.jpg",
            backdropUrl = "https://example.com/backdrop.jpg"
        };
        var json = JsonSerializer.Serialize(detail);
        var apiClient = CreateApiClient(HttpStatusCode.OK, json);

        var result = await apiClient.GetTitleDetailsAsync("tt0133093");

        Assert.NotNull(result);
        Assert.Equal("tt0133093", result.Id);
        Assert.Equal(TitleType.MOVIE, result.Type);
        Assert.Equal("The Matrix", result.Name);
        Assert.Equal(1999, result.Year);
        Assert.Equal(136, result.Runtime);
        Assert.Equal(2, result.Genres.Count);
        Assert.Contains("Action", result.Genres);
    }

    [Fact]
    public async Task GetTitleDetailsAsync_NotFound_ReturnsNull()
    {
        var apiClient = CreateApiClient(HttpStatusCode.NotFound, "");

        var result = await apiClient.GetTitleDetailsAsync("nonexistent");

        Assert.Null(result);
    }

    [Theory]
    [InlineData(null)]
    [InlineData("")]
    [InlineData("  ")]
    public async Task GetTitleDetailsAsync_NullOrEmptyId_ReturnsNullWithoutRequest(string? id)
    {
        var handler = new CountingHttpMessageHandler();
        var httpClient = new HttpClient(handler) { BaseAddress = new Uri("http://localhost:8080") };
        var apiClient = new ApiClient(httpClient);

        var result = await apiClient.GetTitleDetailsAsync(id);

        Assert.Null(result);
        Assert.Equal(0, handler.RequestCount);
    }

    [Fact]
    public async Task GetTitleDetailsAsync_ServerError_ThrowsApiClientException()
    {
        var apiClient = CreateApiClient(HttpStatusCode.InternalServerError, "Server Error");

        var ex = await Assert.ThrowsAsync<ApiClientException>(() => apiClient.GetTitleDetailsAsync("tt0133093"));

        Assert.Equal(HttpStatusCode.InternalServerError, ex.StatusCode);
    }

    [Fact]
    public async Task GetTitleDetailsAsync_Timeout_ThrowsApiClientExceptionWithTimeoutMessage()
    {
        var handler = new TimeoutHttpMessageHandler();
        var httpClient = new HttpClient(handler) { BaseAddress = new Uri("http://localhost:8080") };
        var apiClient = new ApiClient(httpClient);

        var ex = await Assert.ThrowsAsync<ApiClientException>(() => apiClient.GetTitleDetailsAsync("tt0133093"));

        Assert.Contains("timed out", ex.Message, StringComparison.OrdinalIgnoreCase);
    }

    // ─── Helper test doubles ───────────────────────────────────────────────────

    private sealed class FakeHttpMessageHandler : HttpMessageHandler
    {
        private readonly HttpStatusCode _statusCode;
        private readonly string _responseBody;

        public FakeHttpMessageHandler(HttpStatusCode statusCode, string responseBody)
        {
            _statusCode = statusCode;
            _responseBody = responseBody;
        }

        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            var response = new HttpResponseMessage(_statusCode)
            {
                Content = new StringContent(_responseBody, Encoding.UTF8, "application/json")
            };
            return Task.FromResult(response);
        }
    }

    private sealed class CountingHttpMessageHandler : HttpMessageHandler
    {
        public int RequestCount { get; private set; }

        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            RequestCount++;
            return Task.FromResult(new HttpResponseMessage(HttpStatusCode.OK)
            {
                Content = new StringContent("[]", Encoding.UTF8, "application/json")
            });
        }
    }

    private sealed class TimeoutHttpMessageHandler : HttpMessageHandler
    {
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            // Simulate a timeout by throwing TaskCanceledException without cancellation being requested
            throw new TaskCanceledException("Request timed out");
        }
    }

    private sealed class DelayedHttpMessageHandler : HttpMessageHandler
    {
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            await Task.Delay(Timeout.Infinite, cancellationToken);
            return new HttpResponseMessage(HttpStatusCode.OK);
        }
    }
}
