import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.sportifyd.databinding.ItemPopularOrganizersBinding
import com.example.sportifyd.presentation.home.adapter.PopularOrganizers

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

        return itemView.root
    }

}