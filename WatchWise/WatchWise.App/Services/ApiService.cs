using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;

namespace WatchWise.App.Services
{
    public class ApiService
    {
        private readonly HttpClient _httpClient;
        private readonly ILogger<ApiService> _logger;

        public ApiService(HttpClient httpClient, ILogger<ApiService> logger)
        {
            _httpClient = httpClient;
            _logger = logger;
        }

        public async Task<bool> CheckHealthAsync()
        {
            try
            {
                var response = await _httpClient.GetAsync("/actuator/health");
                var result = response.IsSuccessStatusCode;
                
                _logger.LogInformation("API health check: {Status}", result ? "OK" : "FAILED");
                return result;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "API health check failed");
                return false;
            }
        }

        public async Task<string?> GetApiVersionAsync()
        {
            try
            {
                var response = await _httpClient.GetFromJsonAsync<dynamic>("/actuator/info");
                return response?.version?.ToString();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Failed to get API version");
                return null;
            }
        }
    }
}