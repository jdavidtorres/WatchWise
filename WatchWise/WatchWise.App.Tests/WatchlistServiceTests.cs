using WatchWise.App.Models;
using WatchWise.App.Services;
using Xunit;

namespace WatchWise.App.Tests;

public class WatchlistServiceTests
{
    [Fact]
    public async Task AddToWatchlist_ValidTitle_ReturnsTrue()
    {
        // Arrange
        var service = new WatchlistService();
        var title = new TitleLite 
        { 
            Id = "tt123", 
            Name = "Test Movie", 
            Type = TitleType.MOVIE,
            Year = 2023 
        };

        // Act
        var result = await service.AddToWatchlistAsync(title);

        // Assert
        Assert.True(result);
        var isInWatchlist = await service.IsInWatchlistAsync("tt123");
        Assert.True(isInWatchlist);
    }

    [Fact]
    public async Task AddToWatchlist_DuplicateTitle_ReturnsFalse()
    {
        // Arrange
        var service = new WatchlistService();
        var title = new TitleLite 
        { 
            Id = "tt123", 
            Name = "Test Movie", 
            Type = TitleType.MOVIE 
        };

        // Act
        await service.AddToWatchlistAsync(title);
        var result = await service.AddToWatchlistAsync(title);

        // Assert
        Assert.False(result);
    }

    [Fact]
    public async Task RemoveFromWatchlist_ExistingTitle_ReturnsTrue()
    {
        // Arrange
        var service = new WatchlistService();
        var title = new TitleLite 
        { 
            Id = "tt123", 
            Name = "Test Movie", 
            Type = TitleType.MOVIE 
        };
        await service.AddToWatchlistAsync(title);

        // Act
        var result = await service.RemoveFromWatchlistAsync("tt123");

        // Assert
        Assert.True(result);
        var isInWatchlist = await service.IsInWatchlistAsync("tt123");
        Assert.False(isInWatchlist);
    }

    [Fact]
    public async Task GetWatchlist_AfterAddingItems_ReturnsCorrectCount()
    {
        // Arrange
        var service = new WatchlistService();
        var title1 = new TitleLite { Id = "tt1", Name = "Movie 1", Type = TitleType.MOVIE };
        var title2 = new TitleLite { Id = "tt2", Name = "Show 1", Type = TitleType.SHOW };

        // Act
        await service.AddToWatchlistAsync(title1);
        await service.AddToWatchlistAsync(title2);
        var watchlist = await service.GetWatchlistAsync();

        // Assert
        Assert.Equal(2, watchlist.Count);
        Assert.Contains(watchlist, t => t.Id == "tt1");
        Assert.Contains(watchlist, t => t.Id == "tt2");
    }
}