using WatchWise.App.Models;

namespace WatchWise.App.Services;

public class WatchlistService : IWatchlistService
{
    private readonly List<TitleLite> _watchlist = new();

    public Task<bool> AddToWatchlistAsync(TitleLite title)
    {
        if (title == null || string.IsNullOrWhiteSpace(title.Id))
            return Task.FromResult(false);

        if (_watchlist.Any(t => t.Id == title.Id))
            return Task.FromResult(false);

        _watchlist.Add(title);
        return Task.FromResult(true);
    }

    public Task<bool> RemoveFromWatchlistAsync(string titleId)
    {
        if (string.IsNullOrWhiteSpace(titleId))
            return Task.FromResult(false);

        var removed = _watchlist.RemoveAll(t => t.Id == titleId) > 0;
        return Task.FromResult(removed);
    }

    public Task<bool> IsInWatchlistAsync(string titleId)
    {
        if (string.IsNullOrWhiteSpace(titleId))
            return Task.FromResult(false);

        var exists = _watchlist.Any(t => t.Id == titleId);
        return Task.FromResult(exists);
    }

    public Task<List<TitleLite>> GetWatchlistAsync()
    {
        return Task.FromResult(_watchlist.ToList());
    }
}