public interface IRateLimiter
{
    bool AllowRequesty(string clientId);
}