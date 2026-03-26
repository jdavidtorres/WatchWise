using WatchWise.App.Models;
using WatchWise.App.Services;
using Xunit;

namespace WatchWise.App.Tests;

public class SqliteWatchlistServiceTests : IDisposable
{
    private readonly string _tempDbPath;
    private readonly SqliteWatchlistService _service;

    public SqliteWatchlistServiceTests()
    {
        _tempDbPath = Path.Combine(Path.GetTempPath(), $"watchwise_test_{Guid.NewGuid()}.db");
        _service = new SqliteWatchlistService(_tempDbPath);
    }

    public void Dispose()
    {
        if (File.Exists(_tempDbPath))
            File.Delete(_tempDbPath);
    }

    [Fact]
    public async Task AddToWatchlist_ValidTitle_ReturnsTrue()
    {
        var title = new TitleLite { Id = "tt0133093", Name = "The Matrix", Type = TitleType.MOVIE, Year = 1999 };

        var result = await _service.AddToWatchlistAsync(title);

        Assert.True(result);
    }

    [Fact]
    public async Task AddToWatchlist_DuplicateTitle_ReturnsFalse()
    {
        var title = new TitleLite { Id = "tt0133093", Name = "The Matrix", Type = TitleType.MOVIE };

        await _service.AddToWatchlistAsync(title);
        var result = await _service.AddToWatchlistAsync(title);

        Assert.False(result);
    }

    [Fact]
    public async Task AddToWatchlist_NullTitle_ReturnsFalse()
    {
        var result = await _service.AddToWatchlistAsync(null!);

        Assert.False(result);
    }

    [Fact]
    public async Task RemoveFromWatchlist_ExistingTitle_ReturnsTrue()
    {
        var title = new TitleLite { Id = "tt0133093", Name = "The Matrix", Type = TitleType.MOVIE };
        await _service.AddToWatchlistAsync(title);

        var result = await _service.RemoveFromWatchlistAsync("tt0133093");

        Assert.True(result);
        Assert.False(await _service.IsInWatchlistAsync("tt0133093"));
    }

    [Fact]
    public async Task RemoveFromWatchlist_NonExistingTitle_ReturnsFalse()
    {
        var result = await _service.RemoveFromWatchlistAsync("nonexistent");

        Assert.False(result);
    }

    [Fact]
    public async Task IsInWatchlist_AfterAdding_ReturnsTrue()
    {
        var title = new TitleLite { Id = "tt0133093", Name = "The Matrix", Type = TitleType.MOVIE };
        await _service.AddToWatchlistAsync(title);

        var result = await _service.IsInWatchlistAsync("tt0133093");

        Assert.True(result);
    }

    [Fact]
    public async Task IsInWatchlist_NotAdded_ReturnsFalse()
    {
        var result = await _service.IsInWatchlistAsync("tt0133093");

        Assert.False(result);
    }

    [Fact]
    public async Task GetWatchlist_AfterAddingItems_ReturnsCorrectCount()
    {
        var movie = new TitleLite { Id = "tt1", Name = "Movie 1", Type = TitleType.MOVIE, Year = 2020 };
        var show = new TitleLite { Id = "tt2", Name = "Show 1", Type = TitleType.SHOW, Year = 2021 };

        await _service.AddToWatchlistAsync(movie);
        await _service.AddToWatchlistAsync(show);
        var watchlist = await _service.GetWatchlistAsync();

        Assert.Equal(2, watchlist.Count);
        Assert.Contains(watchlist, t => t.Id == "tt1");
        Assert.Contains(watchlist, t => t.Id == "tt2");
    }

    [Fact]
    public async Task GetWatchlist_EmptyDb_ReturnsEmptyList()
    {
        var watchlist = await _service.GetWatchlistAsync();

        Assert.Empty(watchlist);
    }

    [Fact]
    public async Task GetWatchlist_PreservesAllFields()
    {
        var title = new TitleLite
        {
            Id = "tt0133093",
            Type = TitleType.MOVIE,
            Name = "The Matrix",
            Year = 1999,
            PosterUrl = "https://example.com/poster.jpg"
        };
        await _service.AddToWatchlistAsync(title);

        var watchlist = await _service.GetWatchlistAsync();
        var loaded = watchlist.Single();

        Assert.Equal(title.Id, loaded.Id);
        Assert.Equal(title.Type, loaded.Type);
        Assert.Equal(title.Name, loaded.Name);
        Assert.Equal(title.Year, loaded.Year);
        Assert.Equal(title.PosterUrl, loaded.PosterUrl);
    }

    [Fact]
    public async Task GetWatchlist_PersistsAcrossInstances()
    {
        var title = new TitleLite { Id = "tt0133093", Name = "The Matrix", Type = TitleType.MOVIE };
        await _service.AddToWatchlistAsync(title);

        var anotherService = new SqliteWatchlistService(_tempDbPath);
        var watchlist = await anotherService.GetWatchlistAsync();

        Assert.Single(watchlist);
        Assert.Equal("tt0133093", watchlist[0].Id);
    }
}
