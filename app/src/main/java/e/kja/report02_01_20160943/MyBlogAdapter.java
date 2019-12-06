package e.kja.report02_01_20160943;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyBlogAdapter extends BaseAdapter {

    public static final String TAG = "MyBlogAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<NaverBlogDto> list;


    public MyBlogAdapter(Context context, int resource, ArrayList<NaverBlogDto>list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public NaverBlogDto getItem(int position) { return list.get(position);}

    @Override
    public long getItemId(int position) { return list.get(position).get_Id(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //Blog Activity를 위한 Adapter
        Log.d(TAG, "getView with portion" + position);
        View view = convertView;
        ViewHolder viewHolder2 = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder2 = new ViewHolder();
            viewHolder2.blogtitle = view.findViewById(R.id.blogtitle);
            viewHolder2.bloggername = view.findViewById(R.id.bloggername);
            viewHolder2.bloglink = view.findViewById(R.id.bloglink);
            view.setTag(viewHolder2);

        } else {
            viewHolder2 = (MyBlogAdapter.ViewHolder) view.getTag();
        }

        NaverBlogDto dto = list.get(position);

        viewHolder2.blogtitle.setText(dto.getBlogtitle());
        viewHolder2.bloggername.setText(dto.getBloggername());
        viewHolder2.bloglink.setText(dto.getBloggerlink());

        return view;

    }

    public void setList(ArrayList<NaverBlogDto> list){ this.list = list;}

    static class ViewHolder {
        public TextView bloggername = null;
        public TextView bloglink = null;
        public TextView blogtitle = null;
    }





}
