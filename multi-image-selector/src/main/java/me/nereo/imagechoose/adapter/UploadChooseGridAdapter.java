package me.nereo.imagechoose.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

import me.nereo.imagechoose.MultiImageSelectorActivity;
import me.nereo.imagechoose.MultiImageSelectorFragment;
import me.nereo.imagechoose.bean.Image;
import me.nereo.imagechoose.utils.TimeUtil;
import me.nereo.imagechoose.utils.Utils;
import me.nereo.multi_image_selector.R;
import stickygridheaders.StickyGridHeadersGridView;
import stickygridheaders.StickyGridHeadersSimpleAdapter;

/**
 * Created by tangt on 2016/3/4.
 */
public class UploadChooseGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private List<Image> galleryList = new ArrayList<>();
    private Point mPoint = new Point(0, 0);

    private Context context;
    private StickyGridHeadersGridView gridView;
    private List<Image> mSelectedImages = new ArrayList<>();

    public List<Image> getmSelectedImages() {
        return mSelectedImages;
    }

    private LayoutInflater inflater;
    private int mItemSize = 100;
    private MultiImageSelectorFragment fragment;
    public UploadChooseGridAdapter(Context context, StickyGridHeadersGridView gridView, MultiImageSelectorFragment fragment) {
        this.context = context;
        this.gridView = gridView;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    //选择全部照片
    public void chooseAll() {
        Iterator<Image> iterator = galleryList.iterator();
        while (iterator.hasNext()) {
            Image imageAndTime = iterator.next();
            //如果导入则不选中
            imageAndTime.setChooseState("取消全选");

            mSelectedImages.clear();

        }
        notifyDataSetChanged();
    }

    //取消全部选择
    public void giveUpAll() {
        Iterator<Image> iterator = galleryList.iterator();
        while (iterator.hasNext()) {
            Image imageAndTime = iterator.next();
            imageAndTime.setChooseState("全选");
        }
        mSelectedImages.clear();
        notifyDataSetChanged();
    }

    public void setItemSize(int columnWidth) {



//        mItemSize = columnWidth;
//
////        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);
//
//        notifyDataSetChanged();
    }
    @Override
    public long getHeaderId(int position) {
        String ymd = TimeUtil.getDateToString(Long.valueOf(galleryList.get(position).getTime()));
        return TimeUtil.getMillionFromYMD(ymd);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        final HeadHolder headHolder;
        if (convertView == null) {
            headHolder = new HeadHolder();
            convertView = inflater.inflate(R.layout.pf_family_fusion_head, null);
            headHolder.pf_family_first_item_time = (TextView) convertView.findViewById(R.id.pf_family_first_item_time);
            headHolder.pf_family_first_item_count = (TextView) convertView.findViewById(R.id.pf_family_first_item_count);
            headHolder.pf_family_first_item_count.setTextColor(context.getResources().getColor(R.color.title_bg));
            convertView.setTag(headHolder);
        } else {
            headHolder = (HeadHolder) convertView.getTag();
        }
        final Image Image = galleryList.get(position);
        if (Image != null) {
            String time = TimeUtil.getDateToString(Image.getTime());
            headHolder.pf_family_first_item_time.setText("" + time);
            headHolder.pf_family_first_item_count.setText("" + Image.getChooseState());
            headHolder.pf_family_first_item_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = headHolder.pf_family_first_item_count.getText().toString();
                    //先判断是否还有可选择的

                    String ymdTime = TimeUtil.getDateToString(Long.valueOf(Image.getTime()));
                    if (text.equals("全选")) {
                        if (!judgeChoose(ymdTime)) {
                            return;
                        }
                        operation(ymdTime, "add");
                        headHolder.pf_family_first_item_count.setText("取消全选");
                    } else {
                        operation(ymdTime, "remove");
                        headHolder.pf_family_first_item_count.setText("全选");
                    }
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    //判断是否有可上传的
    private boolean judgeChoose(String time) {
        Iterator<Image> iterator = galleryList.iterator();
        while (iterator.hasNext()) {
            Image Image = iterator.next();
            String ymdTime = TimeUtil.getDateToString(Long.valueOf(Image.getTime()));
            if (ymdTime.equals(time)) {
                return true;
            }
        }
        return false;
    }

    private void operation(String time, String type) {
        Iterator<Image> iterator = galleryList.iterator();
        if (type.equals("add")) {
            while (iterator.hasNext()) {
                Image imageAndTime = iterator.next();
                String ymdTime = TimeUtil.getDateToString(Long.valueOf(imageAndTime.getTime()));
                //如果导入则不选中
                if (ymdTime.equals(time)) {
                    imageAndTime.setChooseState("取消全选");
                    if(mSelectedImages.size() >= fragment.getmDesireImageCount()) break;
                    if (!mSelectedImages.contains(imageAndTime)) {
                        mSelectedImages.add(imageAndTime);
                    }
                }
            }
            ((MultiImageSelectorActivity)fragment.getActivity()).showPic(mSelectedImages);
        } else {
            while (iterator.hasNext()) {
                Image Image = iterator.next();
                String ymd = TimeUtil.getDateToString(Long.valueOf(Image.getTime()));
                if (ymd.equals(time)) {
                    Image.setChooseState("全选");
                    if (mSelectedImages.contains(Image)) {
                        mSelectedImages.remove(Image);
                    }
                }
            }
            ((MultiImageSelectorActivity)fragment.getActivity()).showPic(mSelectedImages);
        }

    }

    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if(images != null && images.size()>0){
            galleryList = images;
        }else{
            galleryList.clear();
        }
        Utils.sort(galleryList);
        if(galleryList.size() > 0){
            galleryList.remove(0);
        }
        notifyDataSetChanged();
    }


    class HeadHolder {
        TextView pf_family_first_item_time;
        TextView pf_family_first_item_count;
    }


    @Override
    public int getCount() {
        return galleryList.size();
    }
    public void select(Image image) {
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
        }else{
            mSelectedImages.add(image);
        }
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return galleryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choose_list_item_image, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.indicator = (ImageView) convertView.findViewById(R.id.checkmark);
            viewHolder.mask = convertView.findViewById(R.id.mask);
            viewHolder.iv_choose_area = (ImageView) convertView.findViewById(R.id.iv_choose_area);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Image o = galleryList.get(position);
        bindData(o, viewHolder);
        return convertView;
    }

    public class ViewHolder {
        ImageView image, indicator, iv_choose_area;
        View mask;
    }

    void bindData(final Image data, ViewHolder viewHolder) {
        if (data == null) return;
        viewHolder.indicator.setVisibility(View.VISIBLE);
        if (mSelectedImages.contains(data)) {
            // 设置选中状态
            viewHolder.indicator.setImageResource(R.drawable.btn_selected);
            viewHolder.mask.setVisibility(View.VISIBLE);
        } else {
            // 未选择
            viewHolder.indicator.setImageResource(R.drawable.btn_unselected);
            viewHolder.mask.setVisibility(View.GONE);
        }
        File imageFile = new File(data.path);
        // 显示图片
//                Picasso.with(mContext).setDebugging(true);
        Picasso.with(context)
                .load(imageFile)
                .placeholder(R.drawable.default_error)
                //.error(R.drawable.default_error)
                .resize(100, (int) (100*((MultiImageSelectorActivity)fragment.getActivity()).getDm().density))
                .into(viewHolder.image);
    }
}
