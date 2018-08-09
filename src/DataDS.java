import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by ofeke on 8/4/2018.
 */
public class DataDS {
    public static class Data{
        public int id;
        public Events state;
        public String extra;

        public Data( int id, Events state, String extra){
            this.id = id;
            this.state = state;
            this.extra = extra;
        }
    }

    private static Gson gson = new Gson();

    public static final TypeAdapter<Data> JSON_DSDATA =
            gson.getAdapter(Data.class);
}
