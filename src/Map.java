public class Map 
{
    int map[];
    int size;

    Map(int size, int[] map)
    {
        this.size = size;
        this.map = map;
    }

    public int[][] parseMap()
    {
        int[][] parsedMap = new int[this.size][this.size];
        
        for (int i = 0; i < this.map.length; i++)
        {
            int x = i % this.size;
            int y = (i - x) / this.size;
            parsedMap[x][y] = this.map[i];
        }

        return parsedMap;
    }
}
