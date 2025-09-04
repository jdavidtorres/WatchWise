using Microsoft.Extensions.Logging;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using WatchWise.App.Services;
using WatchWise.App.Models;
using System;
using System.Net.Http;
using System.Threading.Tasks;

namespace WatchWise.App
{
    public static class Program
    {
        public static async Task Main(string[] args)
        {
            Console.WriteLine("WatchWise App - Initializing...");

            // Create a host builder for dependency injection
            var hostBuilder = Host.CreateDefaultBuilder(args)
                .ConfigureServices((context, services) =>
                {
                    // Register services
                    services.AddSingleton<SqliteService>();
                    services.AddHttpClient<ApiService>(client =>
                    {
                        client.BaseAddress = new Uri("http://localhost:8080");
                        client.Timeout = TimeSpan.FromSeconds(30);
                    });
                    services.AddLogging();
                });

            var host = hostBuilder.Build();
            
            // Demonstrate SQLite service
            var sqliteService = host.Services.GetRequiredService<SqliteService>();
            await sqliteService.InitializeAsync();
            Console.WriteLine("✓ SQLite database initialized");

            await sqliteService.SetAppMetaAsync("app_version", "1.0.0");
            var version = await sqliteService.GetAppMetaAsync("app_version");
            Console.WriteLine($"✓ SQLite working - App version: {version}");

            // Demonstrate API service
            var apiService = host.Services.GetRequiredService<ApiService>();
            var isHealthy = await apiService.CheckHealthAsync();
            Console.WriteLine($"✓ API health check: {(isHealthy ? "Connected" : "Failed (backend not running)")}");

            Console.WriteLine("\n🎯 WatchWise App components ready!");
            Console.WriteLine("📱 When MAUI workload is available, this will be a cross-platform mobile app");
            Console.WriteLine("🔗 Ready to connect to Trakt.tv API");
        }
    }
}
