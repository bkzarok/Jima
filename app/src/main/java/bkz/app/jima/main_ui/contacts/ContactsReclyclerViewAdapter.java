package bkz.app.jima.main_ui.contacts;



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
import bkz.app.jima.main_ui.send_ui.SendActivity;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactsReclyclerViewAdapter extends RecyclerView.Adapter<ContactsReclyclerViewAdapter.ViewHolder> {

    private final List<Contact> mValues;
    private Context context;

    public ContactsReclyclerViewAdapter(List<Contact> contacts) {
        mValues = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactsReclyclerViewAdapter.ViewHolder holder, int position) {
        holder.mContact = mValues.get(position);
        Glide.with(holder.contactPhoto.getContext())
                .load(holder.mContact.getPictureUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.contactPhoto);
        holder.contactName.setText(holder.mContact.getName());
        holder.contactPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("View HOlder TAG", "View Holder Button  clicked is click in onBindviewHolder ");
                Intent intent = new Intent(context, SendActivity.class);
                intent.putExtra("userName", holder.mContact.getUserName());
                intent.putExtra("userId", holder.mContact.getUserId());
                intent.putExtra("name", holder.mContact.getName());
                intent.putExtra("photoUrl", holder.mContact.getPictureUrl());
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
        public final ImageView contactPhoto;
        public final TextView contactName;
        public final Button contactPayButton;
        public Contact mContact;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            contactPhoto = (ImageView) view.findViewById(R.id.contact_profilePhoto);

            contactPayButton = (Button) view.findViewById(R.id.contact_pay_button);
            contactName = (TextView) view.findViewById(R.id.contact_name);
            contactPhoto.setOnClickListener(this);
            contactName.setOnClickListener(this);
            contactPayButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView){
              Log.d("View HOlder TAG", "View Holder image is click  ");
            } else if(v instanceof TextView) {
                Log.d("View HOlder TAG", "View Holder text clicked is click  ");
            }
            else if(v instanceof Button)
            {
                Log.d("View HOlder TAG", "View Holder Button clicked is click  ");

            }
        }
        @Override
        public String toString() {
            return super.toString() + " '" + contactName.getText() + "'";
        }
    }
}