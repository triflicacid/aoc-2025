package aoc;

public enum SourceFile
{
    DATA("data.txt"),
    MOCK_DATA("mock_data.txt");

    private final String file;

    SourceFile(String file)
    {
        this.file = file;
    }

    public String toString()
    {
        return file;
    }
}
