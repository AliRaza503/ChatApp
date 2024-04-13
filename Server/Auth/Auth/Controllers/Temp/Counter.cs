namespace Auth.Controllers.Temp;

public class Counter : ICounter
{
    public int Value { get; private set; } = 0;
    public void Increment()
    {
        Value++;
    }
}