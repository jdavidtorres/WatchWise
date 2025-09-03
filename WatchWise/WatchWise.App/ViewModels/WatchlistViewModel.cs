using System.Collections.ObjectModel;
using System.Windows.Input;
using WatchWise.App.Models;
using WatchWise.App.Services;

namespace WatchWise.App.ViewModels;

public class WatchlistViewModel : BaseViewModel
{
    private readonly IWatchlistService _watchlistService;
    private bool _isLoading = false;

    public WatchlistViewModel(IWatchlistService watchlistService)
    {
        _watchlistService = watchlistService;
        WatchlistItems = new ObservableCollection<TitleLite>();
        LoadWatchlistCommand = new Command(async () => await LoadWatchlistAsync());
        RemoveFromWatchlistCommand = new Command<TitleLite>(async (title) => await RemoveFromWatchlistAsync(title));
    }

    public bool IsLoading
    {
        get => _isLoading;
        set => SetProperty(ref _isLoading, value);
    }

    public ObservableCollection<TitleLite> WatchlistItems { get; }

    public ICommand LoadWatchlistCommand { get; }
    public ICommand RemoveFromWatchlistCommand { get; }

    public async Task LoadWatchlistAsync()
    {
        IsLoading = true;
        try
        {
            var items = await _watchlistService.GetWatchlistAsync();
            
            WatchlistItems.Clear();
            foreach (var item in items)
            {
                WatchlistItems.Add(item);
            }
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"Error loading watchlist: {ex.Message}");
        }
        finally
        {
            IsLoading = false;
        }
    }

    private async Task RemoveFromWatchlistAsync(TitleLite? title)
    {
        if (title == null)
            return;

        try
        {
            await _watchlistService.RemoveFromWatchlistAsync(title.Id);
            WatchlistItems.Remove(title);
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"Error removing from watchlist: {ex.Message}");
        }
    }
}

// Command implementation with parameter
public class Command<T> : ICommand
{
    private readonly Func<T?, Task> _execute;
    private readonly Func<T?, bool>? _canExecute;

    public Command(Func<T?, Task> execute, Func<T?, bool>? canExecute = null)
    {
        _execute = execute;
        _canExecute = canExecute;
    }

    public event EventHandler? CanExecuteChanged;

    public bool CanExecute(object? parameter) => _canExecute?.Invoke((T?)parameter) ?? true;

    public async void Execute(object? parameter) => await _execute((T?)parameter);

    public void RaiseCanExecuteChanged() => CanExecuteChanged?.Invoke(this, EventArgs.Empty);
}