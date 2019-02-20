package com.example.ian.paypalpayment.Chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.ian.paypalpayment.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText mInputMessageView;
    private RecyclerView mMessagesView;
    private OnFragmentInteractionListener mListener;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;

    private int count = 0;

    NotificationBadge mBadge;

    private Socket socket;
    {
        try{
            socket = IO.socket("http://192.168.1.224:3000");
        }catch(URISyntaxException e){
            Log.wtf("ERROR-CONNECTION", e.toString()+"");
            throw new RuntimeException(e);
        }
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.wtf("CONNECTION-MSGS", "CONNECT ME PLEASE.");
        socket.connect();
        Log.wtf("AvisActivity", "connection sucessful");
        Log.e("result socket connect", String.valueOf(socket.connected()));


        if(socket.connected()) {
            Toast.makeText(getContext(), "Connected", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "Not Connected", Toast.LENGTH_LONG).show();
        }

        socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_ERROR, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Exception e = (Exception) args[0];
                        Log.wtf("TRANSPORT-ERROR", "Transport error " + e);
                        e.printStackTrace();
                        e.getCause().printStackTrace();
                    }
                });
            }
        });
        //socket.on("message", handleIncomingMessages);
        socket.on("chat message", handleIncomingMessages);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter = new MessageAdapter( mMessages);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessagesView = (RecyclerView) view.findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        mInputMessageView = (EditText) view.findViewById(R.id.message_input);

        mBadge = (NotificationBadge) view.findViewById(R.id.badge);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


    }

    private void sendMessage(){

        String message = mInputMessageView.getText().toString().trim();
        mInputMessageView.setText("");
        Log.wtf("SENDING-MESSAGE", message);
        addMessage(message);
        JSONObject sendText = new JSONObject();
        try{
            Log.wtf("SEND-MESG", message);
            sendText.put("text",message);
            socket.emit("chat message", sendText);
        }catch(JSONException e){

        }

    }

    public void sendImage(String path)
    {
        JSONObject sendData = new JSONObject();
        try{
            sendData.put("image", encodeImage(path));
            Bitmap bmp = decodeImage(sendData.getString("image"));
            addImage(bmp);
            socket.emit("chat message",sendData);
        }catch(JSONException e){

        }
    }

    private void addMessage(String message) {

        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .message(message).build());
        // mAdapter = new MessageAdapter(mMessages);
        mAdapter = new MessageAdapter( mMessages);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();
    }

    private void addImage(Bitmap bmp){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .image(bmp).build());
        mAdapter = new MessageAdapter( mMessages);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();
    }
    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

    private Bitmap decodeImage(String data)
    {
        byte[] b = Base64.decode(data,Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b,0,b.length);
        return bmp;
    }
    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    String imageText;
                    try {

                        mBadge.setNumber(++count);
                        message = data.getString("message").toString();
                        addMessage(message);

                    } catch (JSONException e) {
                        // return;
                    }
                    try {
                        imageText = data.getString("image");
                        addImage(decodeImage(imageText));
                    } catch (JSONException e) {
                        //retur
                    }

                }
            });
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
