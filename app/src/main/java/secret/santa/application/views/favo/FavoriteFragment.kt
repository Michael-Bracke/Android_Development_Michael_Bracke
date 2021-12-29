package secret.santa.application.views.favo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.secret.santa.R
import com.secret.santa.views.FavoItem
import secret.santa.application.models.FavoriteItem

import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.parse.ParseObject
import com.parse.ParseQuery
import com.secret.santa.views.AccountFavoListSSA
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

// door de id v/d layout in de constructor zelf mee te geven hoef je de 'OnCreateView' method
// niet noodzakelijk te declearen aangezien de Id meegeven aan de constr net hetzelfde doet


public class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var accountFavoListSSA : AccountFavoListSSA? = null

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // indien je zaken hebt in je view

        CheckFavoItemsForUser(view)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            accountFavoListSSA = context as AccountFavoListSSA
        }catch (e:ClassCastException ){
            throw java.lang.ClassCastException(context.toString())
        }
    }


    fun CheckFavoItemsForUser(view: View) {

        val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance))
            .getReference("/favoitems");
        val query = ref.orderByChild("UserID").equalTo(FirebaseAuth.getInstance().uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //whenever an actual value has been read
                snapshot.children.forEach {
                    // cast to local db
                    val favoItem = it.getValue(FavoriteItem::class.java)
                    if (favoItem != null) {
                        // whenever the item isnt null AND is of the current user, add it to their view
                        // based on the query
                        adapter.add(FavoItem(favoItem))
                    }
                }
                // maak het visueel zichtbaar door deze adapter ook te binden
                // met de recyclerview
                val recyclerView = view.findViewById<View>(R.id.recyclerView_favoItems) as RecyclerView
                recyclerView.adapter = adapter
            }


        })
    }



}

public fun DeleteFavoItem(id: String) {
    val query = ParseQuery.getQuery<ParseObject>("FavoriteItems")

    // Retrieve the object by id
    query.whereEqualTo("objectId", id)
    var results = query.find();
    results.forEach() {
        it.deleteInBackground()
        // reload function to check update

    }
}


class FavoItem(val favoriteItem: FavoriteItem, val frag: Fragment) : Item<GroupieViewHolder>() {
    // get the layout you want to have each item to be filled in
    // the 'temp' file
    override fun getLayout(): Int {
        return R.layout.row_add_favo_item
    }
    public fun SetFragment(frag: Fragment){

    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.tvFavoItem).text = favoriteItem.Name
        viewHolder.itemView.findViewById<ImageView>(R.id.imgCross).setOnClickListener {
            // set on click listener for deleting an item
            DeleteFavoItem(viewHolder.item.id.toString())
            viewHolder.itemView.context

            (activity as FavoriteFragment).CheckFavoItemsForUser(viewHolder.itemView)
            (getActivity() as FavoriteFragment).CheckFavoItemsForUser(viewHolder.itemView)
        }
    }
}