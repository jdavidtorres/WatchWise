using System;
using System.Net.Http;
using System.Threading.Tasks;

namespace WatchWise.App.Services
{
    public class TitleSearchService
    {
        private readonly HttpClient _httpClient;

        public TitleSearchService()
        {
            _httpClient = new HttpClient { BaseAddress = new Uri("http://localhost:8080") };
        }

        public async Task<string> SearchAsync(string query)
        {
            var response = await _httpClient.GetAsync($"/titles?query={Uri.EscapeDataString(query)}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadAsStringAsync();
        }
    }
}
