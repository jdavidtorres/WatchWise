using System.Windows.Input;
using WatchWise.App.Models;
using WatchWise.App.Services;

namespace WatchWise.App.ViewModels;

public class TitleDetailViewModel : BaseViewModel
{
    private readonly IApiClient _apiClient;
    private readonly IWatchlistService _watchlistService;
    private TitleDetail? _titleDetail;
    private bool _isLoading = false;
    private bool _isInWatchlist = false;
    private string _titleId = string.Empty;

    public TitleDetailViewModel(IApiClient apiClient, IWatchlistService watchlistService)
    {
        _apiClient = apiClient;
        _watchlistService = watchlistService;
        ToggleWatchlistCommand = new Command(async () => await ToggleWatchlistAsync());
    }

    public string TitleId
    {
        get => _titleId;
        set
        {
            if (SetProperty(ref _titleId, value))
            {
                _ = LoadTitleDetailsAsync();
            }
        }
    }

    public TitleDetail? TitleDetail
    {
        get => _titleDetail;
        set => SetProperty(ref _titleDetail, value);
    }

    public bool IsLoading
    {
        get => _isLoading;
        set => SetProperty(ref _isLoading, value);
    }

    public bool IsInWatchlist
    {
        get => _isInWatchlist;
        set => SetProperty(ref _isInWatchlist, value);
    }

    public string WatchlistButtonText => IsInWatchlist ? "Remove from Watchlist" : "Add to Watchlist";

    public ICommand ToggleWatchlistCommand { get; }

    private async Task LoadTitleDetailsAsync()
    {
        if (string.IsNullOrWhiteSpace(TitleId))
            return;

        IsLoading = true;
        try
        {
            TitleDetail = await _apiClient.GetTitleDetailsAsync(TitleId);
            IsInWatchlist = await _watchlistService.IsInWatchlistAsync(TitleId);
            OnPropertyChanged(nameof(WatchlistButtonText));
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"Error loading title details: {ex.Message}");
        }
        finally
        {
            IsLoading = false;
        }
    }

    private async Task ToggleWatchlistAsync()
    {
        if (TitleDetail == null)
            return;

        try
        {
            if (IsInWatchlist)
            {
                await _watchlistService.RemoveFromWatchlistAsync(TitleDetail.Id);
                IsInWatchlist = false;
            }
            else
            {
                var titleLite = new TitleLite
                {
                    Id = TitleDetail.Id,
                    Type = TitleDetail.Type,
                    Name = TitleDetail.Name,
                    Year = TitleDetail.Year,
                    PosterUrl = TitleDetail.PosterUrl
                };
                await _watchlistService.AddToWatchlistAsync(titleLite);
                IsInWatchlist = true;
            }
            OnPropertyChanged(nameof(WatchlistButtonText));
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"Error toggling watchlist: {ex.Message}");
        }
    }
}