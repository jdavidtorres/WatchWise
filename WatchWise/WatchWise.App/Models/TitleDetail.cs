namespace WatchWise.App.Models;

public class TitleDetail
{
    public string Id { get; set; } = string.Empty;
    public TitleType Type { get; set; }
    public string Name { get; set; } = string.Empty;
    public int? Year { get; set; }
    public string? Overview { get; set; }
    public int? Runtime { get; set; }
    public List<string> Genres { get; set; } = new();
    public string? PosterUrl { get; set; }
    public string? BackdropUrl { get; set; }
}