package ds.todoapp;

import com.squareup.otto.Bus;

/**
 * Created by Duygu on 12/05/2017.
 */

public class BusProvider {
    private static final Bus b = new Bus();
    public static Bus getInstance() {
        return b;
    }
    private BusProvider() {}

}
