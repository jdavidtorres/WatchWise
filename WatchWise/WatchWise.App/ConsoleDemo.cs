using WatchWise.App.Models;
using WatchWise.App.Services;

namespace WatchWise.App.Console;

class Program
{
    static async Task Main(string[] args)
    {
        Console.WriteLine("=== WatchWise API Demo ===\n");
        
        try
        {
            var apiClient = new ApiClient();
            var watchlistService = new WatchlistService();
            
            Console.WriteLine("Testing API connection...");
            
            // Test search functionality
            Console.WriteLine("\n1. Testing title search:");
            var searchResults = await apiClient.SearchTitlesAsync("matrix");
            if (searchResults.Any())
            {
                Console.WriteLine($"   ✅ Found {searchResults.Count} results for 'matrix'");
                foreach (var title in searchResults)
                {
                    Console.WriteLine($"      - {title.Name} ({title.Year}) [{title.Type}] ID: {title.Id}");
                }
            }
            else
            {
                Console.WriteLine("   ❌ No results found");
            }
            
            // Test title details
            Console.WriteLine("\n2. Testing title details:");
            var details = await apiClient.GetTitleDetailsAsync("tt0133093");
            if (details != null)
            {
                Console.WriteLine($"   ✅ Retrieved details for: {details.Name}");
                Console.WriteLine($"      Overview: {details.Overview?[..Math.Min(100, details.Overview?.Length ?? 0)]}...");
                Console.WriteLine($"      Runtime: {details.Runtime} minutes");
                Console.WriteLine($"      Genres: {string.Join(", ", details.Genres)}");
            }
            else
            {
                Console.WriteLine("   ❌ Could not retrieve details");
            }
            
            // Test watchlist functionality  
            Console.WriteLine("\n3. Testing watchlist functionality:");
            if (searchResults.Any())
            {
                var firstTitle = searchResults.First();
                var added = await watchlistService.AddToWatchlistAsync(firstTitle);
                Console.WriteLine($"   ✅ Added '{firstTitle.Name}' to watchlist: {added}");
                
                var isInWatchlist = await watchlistService.IsInWatchlistAsync(firstTitle.Id);
                Console.WriteLine($"   ✅ Is in watchlist: {isInWatchlist}");
                
                var watchlist = await watchlistService.GetWatchlistAsync();
                Console.WriteLine($"   ✅ Watchlist contains {watchlist.Count} items");
                
                var removed = await watchlistService.RemoveFromWatchlistAsync(firstTitle.Id);
                Console.WriteLine($"   ✅ Removed from watchlist: {removed}");
            }
            
            // Test type filtering
            Console.WriteLine("\n4. Testing type filtering:");
            var showResults = await apiClient.SearchTitlesAsync("breaking", TitleType.SHOW);
            if (showResults.Any())
            {
                Console.WriteLine($"   ✅ Found {showResults.Count} TV shows for 'breaking'");
                foreach (var show in showResults)
                {
                    Console.WriteLine($"      - {show.Name} ({show.Year}) [{show.Type}]");
                }
            }
            
            Console.WriteLine("\n=== Demo completed successfully! ===");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"\n❌ Error during demo: {ex.Message}");
            Console.WriteLine("Make sure the backend API is running on http://localhost:8080");
        }
        
        Console.WriteLine("\nPress any key to exit...");
        Console.ReadKey();
    }
}