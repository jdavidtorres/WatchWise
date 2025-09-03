namespace WatchWise.App.Models;

public class TitleLite
{
    public string Id { get; set; } = string.Empty;
    public TitleType Type { get; set; }
    public string Name { get; set; } = string.Empty;
    public int? Year { get; set; }
    public string? PosterUrl { get; set; }
}