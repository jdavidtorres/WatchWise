using WatchWise.App.Models;

namespace WatchWise.App.Services;

public interface IWatchlistService
{
    Task<bool> AddToWatchlistAsync(TitleLite title);
    Task<bool> RemoveFromWatchlistAsync(string titleId);
    Task<bool> IsInWatchlistAsync(string titleId);
    Task<List<TitleLite>> GetWatchlistAsync();
}