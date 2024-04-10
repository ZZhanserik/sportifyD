import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sportify.R
import android.widget.BaseAdapter
import com.example.sportify.databinding.ItemPopularOrganizersBinding
import com.example.sportify.presentation.home.adapter.PopularOrganizers

class PopularOrganizersAdapter(private val dataList: List<PopularOrganizers>) : BaseAdapter() {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dataItem = dataList[position]
        val itemView = ItemPopularOrganizersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        itemView.organizerName.text = dataItem.organizerName
        itemView.organizerStatus.text = dataItem.organizerStatus
        itemView.organizerCategory.text = dataItem.category

        Glide.with(parent.context)
            .load(dataItem.photo)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.icon_popular_event_png)
            .error(R.drawable.icon_popular_event_png)
            .into(itemView.organizerPhoto)

        Log.d("Image for ${dataItem.organizerName}", "${dataItem.photo} and ${dataItem.photo.toUri()}")


        return itemView.root
    }

}