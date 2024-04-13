namespace Auth.Controllers.Temp;

public interface ICounter
{
    int Value { get; }
    void Increment();
}