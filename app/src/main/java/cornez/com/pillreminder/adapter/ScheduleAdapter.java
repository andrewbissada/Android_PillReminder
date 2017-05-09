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
import cornez.com.pillreminder.activities.Schedule;



public class ScheduleAdapter extends BaseAdapter {

    List<PillDetailM> pills;
    Activity context;
    LayoutInflater inflater;

    public ScheduleAdapter(Activity context, List<PillDetailM> pills){
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

            convertView         = inflater.inflate(R.layout.schedule_list_item, parent, false);
            //dev.android.com: "Your code might call findViewById() frequently during the scrolling of ListView, which can slow down performance. Even when the Adapter returns an inflated view for recycling, you still need to look up the elements and update them. A way around repeated use of findViewById() is to use the "view holder" design pattern."
            holder              = new ViewHolder();
            holder.tvPillName   = (TextView)convertView.findViewById(R.id.tv_pill_name);
            holder.tvDoase      = (TextView)convertView.findViewById(R.id.tv_doase);
            holder.tvCompleted  = (TextView)convertView.findViewById(R.id.tv_completed);

            holder.ibMorning    = (ImageView)convertView.findViewById(R.id.ib_morning);
            holder.ibNoon       = (ImageView)convertView.findViewById(R.id.ib_noon);
            holder.ibNight      = (ImageView)convertView.findViewById(R.id.ib_night);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        PillDetailM pill = pills.get(position);

        holder.tvPillName.setText(pill.getPillName());
        holder.tvDoase.setText(pill.getDoase());

        holder.tvCompleted.setText(pill.isCompleted() ? "Yes" : "No");

        holder.ibMorning.setTag(position);
        holder.ibNoon.setTag(position);
        holder.ibNight.setTag(position);

        holder.ibMorning.setOnClickListener(clickListener);
        holder.ibNoon.setOnClickListener(clickListener);
        holder.ibNight.setOnClickListener(clickListener);

        holder.ibMorning.setImageResource(R.drawable.uncheckedbox);
        holder.ibNoon.setImageResource(R.drawable.uncheckedbox);
        holder.ibNight.setImageResource(R.drawable.uncheckedbox);


        if (pill.isMorningPending())
            holder.ibMorning.setImageResource(R.drawable.uncheckedbox);

        else
            holder.ibMorning.setImageResource(R.drawable.checkedbox);

        if (pill.isNoonPending())
            holder.ibNoon.setImageResource(R.drawable.uncheckedbox);

        else
            holder.ibNoon.setImageResource(R.drawable.checkedbox);

        if (pill.isNightPending())
            holder.ibNight.setImageResource(R.drawable.uncheckedbox);

        else
            holder.ibNight.setImageResource(R.drawable.checkedbox);

        String frequency = pill.getFrequency().split("x")[0];

        if (frequency.equals("1")){

            if (!pill.isMorningPending())
                holder.tvCompleted.setText("YES");

            else
                holder.tvCompleted.setText("NO");

            holder.ibMorning.setVisibility(View.VISIBLE);

            holder.ibNoon.setVisibility(View.INVISIBLE);
            holder.ibNight.setVisibility(View.INVISIBLE);

        }else if (frequency.equals("2")){

            if (!pill.isMorningPending() && !pill.isNoonPending())
                holder.tvCompleted.setText("YES");

            else
                holder.tvCompleted.setText("NO");


            holder.ibNoon.setVisibility(View.VISIBLE);
            holder.ibMorning.setVisibility(View.VISIBLE);

            holder.ibNight.setVisibility(View.INVISIBLE);

        }else if (frequency.equals("3")){

            if (!pill.isMorningPending()
                    && !pill.isNoonPending()
                    && !pill.isNightPending()){

                holder.tvCompleted.setText("YES");

            }else{
                holder.tvCompleted.setText("NO");
            }

            holder.ibNoon.setVisibility(View.VISIBLE);
            holder.ibNight.setVisibility(View.VISIBLE);
            holder.ibNight.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public static class ViewHolder{
        TextView tvPillName;
        TextView tvDoase;
        TextView tvCompleted;

        ImageView ibMorning;
        ImageView ibNoon;
        ImageView ibNight;
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

            if (view.getId() == R.id.ib_morning){

                Schedule activity = (Schedule)context;

                ImageView ibMorning = (ImageView) view;

                if (ibMorning.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.uncheckedbox)
                        .getConstantState()){

                    ibMorning.setImageResource(R.drawable.checkedbox);

                    int position        = (int)view.getTag();
                    PillDetailM pill    = pills.get(position);

                    pill.setMorningPending(false);
                    pills.set(position, pill);

                    activity.updateRecord(pill.getId(), position, AndroidOpenDBHelper.COLUMN_NAME_MORNING_STATUS_PENDING, "no", pill);

                    String frequency = pill.getFrequency().split("x")[0];

                    if (frequency.equals("1")){
                        //updateCompletedValue(position, true);
                    }

                }else{

                    ibMorning.setImageResource(R.drawable.uncheckedbox);

                    int position        = (int)view.getTag();
                    PillDetailM pill    = pills.get(position);

                    pill.setMorningPending(true);
                    pills.set(position, pill);

                    activity.updateRecord(pill.getId(), position, AndroidOpenDBHelper.COLUMN_NAME_MORNING_STATUS_PENDING, "yes", pill);

                    String frequency = pill.getFrequency().split("x")[0];

                    if (frequency.equals("1")){
                        //updateCompletedValue(position, false);
                    }

                }

            }else if (view.getId() == R.id.ib_noon){

                Schedule activity = (Schedule)context;

                int position        = (int)view.getTag();
                PillDetailM pill    = pills.get(position);

                ImageView ibMorning = (ImageView) view;

                if (pill.isMorningPending()){

                    Toast.makeText(context, "Please take Morning dosage first", Toast.LENGTH_SHORT).show();

                }else {

                    if (ibMorning.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.uncheckedbox)
                            .getConstantState()) {

                        ibMorning.setImageResource(R.drawable.checkedbox);

                        pill.setNoonPending(false);
                        pills.set(position, pill);

                        activity.updateRecord(pill.getId(), position, AndroidOpenDBHelper.COLUMN_NAME_NOON_STATUS_PENDING, "no", pill);

                        String frequency = pill.getFrequency().split("x")[0];

                        if (frequency.equals("2")){
                            //updateCompletedValue(position, true);
                        }


                    }else{

                        ibMorning.setImageResource(R.drawable.uncheckedbox);

                        pill.setNoonPending(true);
                        pills.set(position, pill);

                        activity.updateRecord(pill.getId(), position, AndroidOpenDBHelper.COLUMN_NAME_NOON_STATUS_PENDING, "yes", pill);

                        String frequency = pill.getFrequency().split("x")[0];

                        if (frequency.equals("2")){
                            //updateCompletedValue(position, false);
                        }

                    }
                }

            } else if (view.getId() == R.id.ib_night) {

                int position        = (int)view.getTag();
                PillDetailM pill    = pills.get(position);

                if (pill.isMorningPending()){

                    Toast.makeText(context, "Please take Morning dosage first", Toast.LENGTH_SHORT).show();

                }else if (pill.isNoonPending()){

                    Toast.makeText(context, "Please take AfterNoon dosage first", Toast.LENGTH_SHORT).show();

                }else {

                    Schedule activity = (Schedule)context;

                    ImageView ibMorning = (ImageView) view;

                    if (ibMorning.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.uncheckedbox)
                            .getConstantState()) {

                        ibMorning.setImageResource(R.drawable.checkedbox);

                        pill.setNightPending(false);
                        pills.set(position, pill);

                        activity.updateRecord(pill.getId(), position, AndroidOpenDBHelper.COLUMN_NAME_NIGHT_STATUS_PENDING, "no", pill);

                        //updateCompletedValue(position, true);

                    }else{

                        ibMorning.setImageResource(R.drawable.uncheckedbox);

                        pill.setNightPending(true);
                        pills.set(position, pill);

                        //updateCompletedValue(position, false);

                        activity.updateRecord(pill.getId(), position, AndroidOpenDBHelper.COLUMN_NAME_NIGHT_STATUS_PENDING, "yes", pill);
                    }
                }

            }
        }
    };
}
