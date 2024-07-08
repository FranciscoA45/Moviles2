package com.example.foundit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.example.foundit.databinding.FragmentHomeContenidoBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: FragmentHomeContenidoBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var db: NoteDataBaseHelper
    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
        db = NoteDataBaseHelper(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeContenidoBinding.inflate(inflater)
        val view = binding.root

        // Inicializar adaptador y asignarlo al RecyclerView
        notesAdapter = NotesAdapter(mutableListOf(), context)
        binding.notesReclyclerView.adapter = notesAdapter
        binding.notesReclyclerView.layoutManager = LinearLayoutManager(context)

        // Inicialización de las vistas
        drawerLayout = view.findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = view.findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val profileIv: ImageView = view.findViewById(R.id.profileIv)
        profileIv.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val settingsIv: ImageView = view.findViewById(R.id.rightIcon)
        settingsIv.setOnClickListener {
            val intent = Intent(activity, Settings::class.java)
            startActivity(intent)
        }

        // Inicializar y configurar el FloatingActionButton
        val addButton: FloatingActionButton = view.findViewById(R.id.addPublicacion)
        addButton.setOnClickListener {
            val intentPublicaciones = Intent(activity, addNoteActivity::class.java)
            startActivity(intentPublicaciones)
        }

        fetchNotes()

        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_two -> {
                val intentSensibilidad = Intent(activity, ProfileFragment::class.java)
                startActivity(intentSensibilidad)
                return true
            }
            R.id.nav_item_one -> {
                val intentNotificaciones = Intent(activity, NotificationFragment::class.java)
                startActivity(intentNotificaciones)
                return true
            }
            R.id.nav_item_three -> {
                val intentPublicaciones = Intent(activity, Publicaciones::class.java)
                startActivity(intentPublicaciones)
                return true
            }
            else -> {
                Toast.makeText(activity, "Item desconocido seleccionado", Toast.LENGTH_SHORT).show()
                return false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchNotes()
    }

    private fun fetchNotes() {
        val url = "http://192.168.1.72:8000/api/posts" // Reemplace con su URL de API
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val notes = db.getAllNotes(response)
                notesAdapter = NotesAdapter(notes.toMutableList(), context)
                binding.notesReclyclerView.adapter = notesAdapter
            },
            Response.ErrorListener { error ->
                // Manejar error
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
