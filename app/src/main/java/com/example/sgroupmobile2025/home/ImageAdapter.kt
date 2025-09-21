import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.databinding.ItemImgBinding
import com.example.sgroupmobile2025.home.Product


class ImageAdapter(
    private val context: Context,
    private val products: List<Product>
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemImgBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.binding.imgProduct.setImageResource(product.imageRes)
        holder.binding.txtBrand.text = product.name
        holder.binding.txtName.text = product.description
        holder.binding.txtPrice.text = product.price
    }

    override fun getItemCount(): Int = products.size
}

