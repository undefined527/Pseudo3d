public class App {
    public static final Map[] MAPS = new Map[] 
    {
        new Map(10, new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1  
        }),
    
        new Map(10, new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 1, 1, 1, 0, 1, 0, 0, 1,
            1, 0, 0, 0, 1, 0, 1, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 1, 0, 1, 0, 1, 1, 0, 1,
            1, 0, 1, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 1, 1, 1, 0, 1, 1, 0, 1,
            1, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1  
        }),
    
        new Map(10, new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 1, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1 
        })
    };

    final static Map currentMap = MAPS[2]; // 0 = empty, 1 = maze, 2 = room
    public static void main(String[] args) throws Exception {
        new GameFrame();
    }
}
