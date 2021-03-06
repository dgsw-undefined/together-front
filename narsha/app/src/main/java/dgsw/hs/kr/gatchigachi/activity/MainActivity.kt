package dgsw.hs.kr.gatchigachi.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.*
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import dgsw.hs.kr.gatchigachi.LoginActivity
import dgsw.hs.kr.gatchigachi.MakeTeamActivity
import dgsw.hs.kr.gatchigachi.R
import dgsw.hs.kr.gatchigachi.TrustActivity
import dgsw.hs.kr.gatchigachi.adapter.TeamGridAdapter
import dgsw.hs.kr.gatchigachi.database.DBHelper
import dgsw.hs.kr.gatchigachi.model.Team
import dgsw.hs.kr.gatchigachi.model.TeamMember
import dgsw.hs.kr.gatchigachi.model.User
import dgsw.hs.kr.gatchigachi.network.Network
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val network =  Network()
    lateinit var myDb : DBHelper
    var userIdx = 0
    var teamId = 0
    var user : User? = null

    override fun onResume() {
        super.onResume()

        if(myDb.selectMyInfo()!!.idx == userIdx.toLong()){
            user = myDb.selectMyInfo()
            user = myDb.selectUserById(userIdx)
            val teams = myDb.selectAllMyTeam()
            Log.e("teams",teams.toString())
            btn_do_trust.text = "정보수정"
            log_out.visibility = View.VISIBLE
            setView(teams)
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_main)
        myDb = DBHelper(this)

        userIdx = intent.getIntExtra("userIdx",0)
        teamId = intent.getIntExtra("teamId", 0)

        Log.e("userId",userIdx.toString())

        if(myDb.selectMyInfo()!!.idx == userIdx.toLong()){
            user = myDb.selectMyInfo()
            val teams = myDb.selectAllMyTeam()
            Log.e("teams",teams.toString())
            btn_do_trust.text = "정보수정"
            log_out.visibility = View.VISIBLE
            setView(teams)
        }else{
            user = myDb.selectUserById(userIdx)
            val teams = myDb.selectAllTeamByUserIdx(userIdx)
            btn_do_trust.text = "Trust"
            add_team.visibility = View.INVISIBLE
            log_out.visibility = View.GONE
            setView(teams)
        }

        if(teamId!=0){
            btn_do_trust.text = "추가"
        }

        log_out.setOnClickListener {
            myDb.deleteMyInfo()
            myDb.deleteMyTeam()
            myDb.deleteToken()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_do_trust.setOnClickListener{
            if(teamId != 0){
                val member = TeamMember(
                        null,
                        teamId.toLong(),
                        userIdx.toLong(),
                        user!!.name,
                        user!!.field,
                        null,
                        null,
                        null,
                        null,
                        null
                )
                network.addTeamMember(member,myDb,this)
                finish()
            }
        }

        val timer = Timer()

        user_profile_main.background = ShapeDrawable(OvalShape())
        user_profile_main.clipToOutline = true
        user_profile_main.bringToFront()



        trust_count.setOnClickListener {
//            val nextIntent = Intent(this, TrustActivity::class.java)
//            startActivity(nextIntent)
            Log.e("aaa","AA")
            Toast.makeText(this,"아직 구현이 덜됨..........",Toast.LENGTH_SHORT).show()
        }

        truster_count.setOnClickListener {
//            val nextIntent = Intent(this, TrustActivity::class.java)
//            startActivity(nextIntent)
            Log.e("aaa","AA")
            Toast.makeText(this,"아직 구현이 덜됨.......",Toast.LENGTH_SHORT).show()
        }

        add_team.setOnClickListener {
            val nextIntent = Intent(this, MakeTeamActivity::class.java)
            startActivity(nextIntent)
        }

        to_search.setOnClickListener {
            val nextIntent = Intent(this, LookForActivity::class.java)
            startActivity(nextIntent)
        }

    }

    private fun setView(teams:ArrayList<Team>){
        user_name.text = user!!.name
        user_position.text = user!!.position
        user_mail.text = user!!.email
        user_git.text = user!!.github
        user_phone.text = user!!.phone

        Glide.with(this)
                .load(user!!.profile)
                .into(user_profile_main)

        var teamAdapter = TeamGridAdapter(this, teams)
        team_grid_view.adapter = teamAdapter

        for (team in teams)(
                network.getTeamMember(team.id!!.toInt(),myDb,this,0)
        )

//        if( 1 == intent.getIntExtra("isTeamListEmpty", 0)){
//            val nextIntent = Intent(this, LookForActivity::class.java)
//            startActivity(nextIntent)
//        }
    }

}
//        btn_trust.setOnClickListener {
//            val nextIntent = Intent(this, DetailTeamActivity::class.java)
//            startActivity(nextIntent)
//        }


//        btn_open_detail.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked){
//                bottom_aaa.startAnimation(down_anim)
//                timer.schedule(timerTask { this }, 5000)
//                bottom_aaa.y = bottom_aaa.y+400
//
//                detail.visibility = VISIBLE
//
//            } else{
//                bottom_aaa.startAnimation(up_anim)
//                timer.schedule(timerTask { this }, 5000)
//                bottom_aaa.y = bottom_aaa.y-400
//
//                detail.visibility = INVISIBLE
//
//
//            }
//
//        }

//        val toggle = ActionBarDrawerToggle(
//                this, wrap, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        wrap.addDrawerListener(toggle)
//        toggle.syncState()
//
//        nav_view.setNavigationItemSelectedListener(this)
//
//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_camera -> {
//                // Handle the camera action
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
