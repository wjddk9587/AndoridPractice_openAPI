package e.kja.report02_01_20160943;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyRestaurantAdapter extends BaseAdapter {
    public static final String TAG = "MyRestaurantAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<NaverLocalDto> list;


    public MyRestaurantAdapter(Context context, int resource, ArrayList<NaverLocalDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return list.size(); }

    public NaverLocalDto getItem(int position) { return list.get(position); }

    public long getItemId(int position) { return list.get(position).get_id();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with portion" + position);
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.address = view.findViewById(R.id.address);
            viewHolder.telephone = view.findViewById(R.id.telephone);
            viewHolder.category = view.findViewById(R.id.category);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        NaverLocalDto dto = list.get(position);

        viewHolder.title.setText(dto.getTitle());
        viewHolder.category.setText(dto.getCategory());
        viewHolder.telephone.setText(dto.getTelephone());
        viewHolder.address.setText(dto.getAddress());

        return view;
    }

    public void setList(ArrayList<NaverLocalDto> list){ this.list = list;}

    static class ViewHolder {
        public TextView title = null;
        public TextView address = null;
        public TextView telephone = null;
        public TextView category = null;
    }
}