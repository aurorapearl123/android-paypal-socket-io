package com.example.ian.paypalpayment;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.ian.paypalpayment.Adapter.ClientAdapter;
import com.example.ian.paypalpayment.Adapter.ItemAdapter;
import com.example.ian.paypalpayment.Config.Config;

import com.example.ian.paypalpayment.Model.Item;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.nex3z.notificationbadge.NotificationBadge;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

public class HomePageActivity extends AppCompatActivity implements SearchView.OnSuggestionListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, BaseSliderView.OnSliderClickListener //SwipyRefreshLayout.OnRefreshListener,
{

    private RecyclerView recyclerView, recyclerViewIcon;
    private ClientAdapter clientAdapter;
    private ItemAdapter iconClientAdapter;
    private ArrayList<Item> items;

    private boolean loading = true;
    private int count = 1;
    private int countBadge = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ImageView imageView;

    //@BindView(R.id.id_text_search) EditText txt_search;

    public static final int DISMISS_TIMEOUT = 2000;

   // public SwipyRefreshLayout swipyrefreshlayout;

    private Menu menu;

    NotificationBadge mBadge;
    //set socket io
    private Socket socket;
    {
        try{
            socket = IO.socket(Config.SOCKET_SERVER);
        }catch(URISyntaxException e){
            Log.wtf("ERROR-CONNECTION", e.toString()+"");
            throw new RuntimeException(e);
        }
    }

    //private SliderLayout mDemoSlider;
    private SliderLayout mDemoSlider;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //override appbar
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ButterKnife.bind(this);

        //txt_search.setSelected(false);
        //txt_search.setError("error");
//        txt_search.setFocusableInTouchMode(false);
//        txt_search.setFocusable(false);
//        txt_search.setFocusableInTouchMode(true);
//        txt_search.setFocusable(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    //showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                   // hideOption(R.id.action_info);
                }
            }
        });
        //Get the default actionbar instance
//        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
//        mActionBar.setDisplayShowHomeEnabled(false);
//        mActionBar.setDisplayShowTitleEnabled(false);
//
////Initializes the custom action bar layout
//        LayoutInflater mInflater = LayoutInflater.from(this);
//        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
//        mActionBar.setCustomView(mCustomView);
//        mActionBar.setDisplayShowCustomEnabled(true);
//
//        mBadge = (NotificationBadge) findViewById(R.id.badge);
//
//         imageView = (ImageView) findViewById(R.id.id_payment_icon);
//
//         imageView.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 startActivity(new Intent(getApplicationContext(), SocketActivity.class));
//             }
//         });



        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //icon
        recyclerViewIcon = (RecyclerView) findViewById(R.id.recyclerViewIcon);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items = new ArrayList<>();
        //iconClientAdapter = new ItemAdapter(this, items);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //recyclerView.setAdapter(clientAdapter);

        //create data for icon

        //iconClientAdapter = new ClientAdapter(this, clients);

        //recyclerViewIcon.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager manager = new GridLayoutManager(getBaseContext(), 2);
        recyclerViewIcon.setLayoutManager(manager);
        //recyclerViewIcon.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(iconClientAdapter);
        //Item(String id, String user_id, String category_id, String name, String description, String model, String price, String group_size, String image_path)
        //String id, String first_name, String middle_name, String last_name, String phone, String email, String profile
        Item a = new Item("1", "1","2", "Friesen", "Emmett", "425.595.0961 x06853", "409", "5", "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg");
        Item a2 = new Item("2", "1","2", "Friesen", "Emmett", "425.595.0961 x06853", "409", "5", "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg");

        Item a3 = new Item("1", "1","2", "Friesen", "Emmett", "425.595.0961 x06853", "409", "5", "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg");
        Item a4 = new Item("1", "1","2", "Friesen", "Emmett", "425.595.0961 x06853", "409", "5", "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg");

        Item a5 = new Item("1", "1","2", "Friesen", "Emmett", "425.595.0961 x06853", "409", "5", "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg");

        Item a6 = new Item("1", "1","2", "Friesen", "Emmett", "425.595.0961 x06853", "409", "5", "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg");
        items = new ArrayList<>();
        items.add(a);
        items.add(a2);
        items.add(a3);
        items.add(a4);
        items.add(a5);
        items.add(a6);

        iconClientAdapter = new ItemAdapter(this, items);
        recyclerViewIcon.setAdapter(iconClientAdapter);

       // mBadge.setNumber(30);

        //recyclerView.setAdapter(clientAdapter);

        //createClientList(1);

        //paginate();

        //setsocket event
        socket.connect();

        socket.on("chat message", handleIncomingMessages);

       // swipyrefreshlayout = (SwipyRefreshLayout) findViewById(R.id.id_swipyrefreshlayout);

       // swipyrefreshlayout.setOnRefreshListener(this);



        //swipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        //swipyrefreshlayout.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");


        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.image_1);
        file_maps.put("Big Bang Theory",R.drawable.image_2);
        file_maps.put("House of Cards",R.drawable.image_3);
        file_maps.put("Game of Thrones", R.drawable.image_4);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        //next



    }

    public void paginate()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)) {
                    //direction integers: -1 for up, 1 for down, 0 will always return false.
                    if(loading) {
                        loading = false;

                        Log.wtf("SCROLL-BUTTOM", "YOU REACH ME DEAR."+ ++count);
                        //createClientList(++count);
                    }
                }
            }
        });

    }


//    public void createClientList(int page){
//        String token = RICRest.getInstance().getPreferences(getApplicationContext(), RICRest.TOKEN);
//        RICRest.getInstance().addAuthHeaders(token);
//        RICRest.getInstance().clientList(clients, clientAdapter, page);
//        //Client(String id, String first_name, String middle_name, String last_name, String phone, String email)
//        //https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg
//        //Client(String id, String first_name, String middle_name, String last_name, String phone, String email, String profile)
//
////        String image = "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg";
////
////        Client client = new Client("1", "ian", "uayan", "rosales", "0943324", "gso8412@gmail.com", image);
////        clients.add(client);
//
////
////        clientAdapter.notifyDataSetChanged();
//
//        loading = true;
//    }

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    String imageText;
                    try {

                        mBadge.setNumber(++countBadge);
                        message = data.getString("message").toString();
                        //addMessage(message);

                    } catch (JSONException e) {
                        // return;
                    }
                    try {
                        imageText = data.getString("image");
                        //addImage(decodeImage(imageText));
                    } catch (JSONException e) {
                        //retur
                    }

                }
            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
      //  getMenuInflater().inflate(R.menu.menu_scrolling, menu);

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.payment, menu);
//        MenuItem menuItem = menu.findItem(R.id.id_search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Type something...");
//        searchView.setOnSuggestionListener(this);
//        searchView.setOnQueryTextListener(this);
//        searchView.setOnCloseListener(this);
     //   hideOption(R.id.action_info);

//        int id = searchView.getContext()
//                .getResources()
//                .getIdentifier("android:id/search_src_text", null, null);
//        EditText editText = (EditText) searchView.findViewById(id);
//        Log.wtf("SEARCH", editText.getText().toString()+"");
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        Log.wtf("TEXT-SELECT", i+"");
        return false;
    }

    @Override
    public boolean onSuggestionClick(int i) {
        Log.wtf("TEXT-CLICK", i+"");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.wtf("TEXT-SUBMIT", s+"");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(s.length() < 4) {

        }
        else {
            //searchServer(s);
            Log.wtf("TEXT-CHANGE", s+"");
        }

        return false;
    }

    @Override
    public boolean onClose() {
        Log.wtf("TEXT-CLOSE", "closing");
        return false;
    }

//    public void searchServer(String search) {
//        String token = RICRest.getInstance().getPreferences(getApplicationContext(), RICRest.TOKEN);
//        RICRest.getInstance().addAuthHeaders(token);
//        RICRest.getInstance().searchToServer(clients, clientAdapter,search);
//
//    }

//    @Override
//    public void onRefresh(final SwipyRefreshLayoutDirection direction) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                HomePageActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
//                            createClientList(++count);
//                        }
//                        else {
//                            Log.wtf("resfresh", "resfresh me");
//                        }
//                        swipyrefreshlayout.setRefreshing(false);
//
//                    }
//                });
//            }
//        }, DISMISS_TIMEOUT);
//    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
       item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }
}
