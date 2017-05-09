package cornez.com.pillreminder.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cornez.com.pillreminder.Model.PillDetailM;
import cornez.com.pillreminder.R;
import cornez.com.pillreminder.activities.AndroidOpenDBHelper;
import cornez.com.pillreminder.activities.DeleteA;
import cornez.com.pillreminder.activities.Schedule;



public class DeletePillAdapter extends BaseAdapter {

    List<PillDetailM> pills;
    Activity context;
    LayoutInflater inflater;

    public DeletePillAdapter(Activity context, List<PillDetailM> pills){

        this.context    = context;
        this.pills      = pills;

        inflater        = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pills.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){

            convertView         = inflater.inflate(R.layout.delete_pill_list_item, parent, false);
            holder              = new ViewHolder();
            holder.tvPillName   = (TextView)convertView.findViewById(R.id.tv_pill_title);

            holder.ibDelete     = (ImageButton)convertView.findViewById(R.id.btn_delete);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        PillDetailM pill = pills.get(position);

        holder.ibDelete.setTag(position);

        holder.tvPillName.setText(pill.getPillName());
        holder.ibDelete.setOnClickListener(clickListener);

        return convertView;
    }

    public static class ViewHolder{
        TextView tvPillName;

        ImageView ibDelete;
    }

    public void updateCompletedValue(int position, boolean status){

        Schedule activity   = (Schedule)context;
        View view           = (View) activity.listView.getChildAt(position);
        TextView tvCompleted= (TextView)view.findViewById(R.id.tv_completed);
        tvCompleted.setText(status ? "YES" : "NO");

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                DeleteA activity = (DeleteA) context;

                int position = (int) view.getTag();
                PillDetailM pill = pills.get(position);

                pill.setMorningPending(false);
                pills.set(position, pill);

                activity.updateRecord(pill);
        }
    };
}
