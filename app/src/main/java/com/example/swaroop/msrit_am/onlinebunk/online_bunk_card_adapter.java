package com.example.swaroop.msrit_am.onlinebunk;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.swaroop.msrit_am.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by swaroop on 12/14/2016.
 */

public class online_bunk_card_adapter extends RecyclerView.Adapter<online_bunk_card_adapter.ViewHolder>{

    private JSONArray arr;

    public online_bunk_card_adapter(JSONArray obj) throws JSONException {
        this.arr = new JSONArray(obj.toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView code;
        private TextView subject;
        private TextView bunks;
        private TextView risky;
        private TextView oneMore;
        private TextView percent;
        CardView cc;


        public ViewHolder(View itemView) {
            super(itemView);

            cc = (CardView) itemView.findViewById(R.id.card);

            //code = (TextView) itemView.findViewById(R.id.code);
            subject = (TextView) itemView.findViewById(R.id.subject);
            bunks = (TextView) itemView.findViewById(R.id.bunks);
            percent=(TextView) itemView.findViewById(R.id.percentage);
            risky=(TextView) itemView.findViewById(R.id.risky);
            oneMore=(TextView) itemView.findViewById(R.id.oneMore);


        }
    }

    @Override
    public online_bunk_card_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(online_bunk_card_adapter.ViewHolder holder, int position) {
        JSONObject childJSONObject = null;
        try {
            childJSONObject = arr.getJSONObject(position);
            String name = childJSONObject.getString("name");
            String name1 = childJSONObject.getString("code");
            int name2= childJSONObject.getInt("bunks");
            int riskBunks=childJSONObject.getInt("risky");
            int po = childJSONObject.getInt("possible");
            int oneMore=childJSONObject.getInt("OneMoreBunkP");
            String p=childJSONObject.getString("percentage");
            holder.subject.setText(name+" "+name1);
            //holder.code.setText(name1);
            holder.bunks.setText("Classes you can skip:"+name2);
            holder.percent.setText("Current Percentage:"+p);
            holder.risky.setText("MAX. classes you can skip(75%):"+riskBunks);
            holder.oneMore.setText("If you skip one class % will become:"+oneMore+"%");
            if(po != 1)
                holder.cc.setCardBackgroundColor(Color.parseColor("#CFD8DC"));
            else
                holder.cc.setCardBackgroundColor(Color.parseColor("#FFFFFF"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return arr.length();
    }


}
