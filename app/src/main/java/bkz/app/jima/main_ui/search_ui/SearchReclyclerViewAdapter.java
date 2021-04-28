package bkz.app.jima.main_ui.search_ui;



import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import bkz.app.jima.R;
import bkz.app.jima.data.dummy.DummyContent.DummyItem;
import bkz.app.jima.data.model.Contact;
import bkz.app.jima.data.model.User;
import bkz.app.jima.main_ui.send_ui.SendActivity;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchReclyclerViewAdapter extends RecyclerView.Adapter<SearchReclyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private

    Context context;
    public SearchReclyclerViewAdapter(List<User> users) {
        mValues = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchReclyclerViewAdapter.ViewHolder holder, int position) {
        holder.mUser = mValues.get(position);
        Glide.with(holder.userPhoto.getContext())
                .load(holder.mUser.getPictureUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userPhoto);
        holder.userName.setText(holder.mUser.getName());
        holder.userPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("View HOlder TAG", "View Holder Button  clicked is click in onBindviewHolder ");

                Intent intent = new Intent(context, SendActivity.class);
                intent.putExtra("userName", holder.mUser.getUserName());
                intent.putExtra("userId", holder.mUser.getUserId());
                intent.putExtra("name", holder.mUser.getName());
                intent.putExtra("photoUrl", holder.mUser.getPictureUrl());
                context.startActivity(intent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView userPhoto;
        public final TextView userName;
        public final Button userPayButton;
        public User mUser;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            userPhoto = (ImageView) view.findViewById(R.id.search_profilePhoto);

            userPayButton = (Button) view.findViewById(R.id.search_pay_button);
            userName = (TextView) view.findViewById(R.id.search_name);
            userPhoto.setOnClickListener(this);
            userName.setOnClickListener(this);
            userPayButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
//            if (v instanceof ImageView){
//                Log.d("View HOlder TAG", "View Holder image is click  ");
//            } else if(v instanceof TextView) {
//                Log.d("View HOlder TAG", "View Holder text clicked is click  ");
//            }
//            else if(v instanceof Button)
//            {
//                Log.d("View HOlder TAG", "View Holder Button clicked is click  ");
//
//            }
        }
        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }
    }
}