package codepath.com.nytimessearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nb0 on 9/15/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article>{

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get data item for the position
        Article article = this.getItem(position);


        //get view. Chk null
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result,parent,false);

        }


        //imageview
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivimage);

        //Clear out imageview from last time/iteration
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(article.getHeadLine());

        //populate thumbnail image

        String thumbnail = article.getThumbNail();

        if(!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);

        }

        return convertView;










    }
}
