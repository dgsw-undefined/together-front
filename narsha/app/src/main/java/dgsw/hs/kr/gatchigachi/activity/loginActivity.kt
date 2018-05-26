package dgsw.hs.kr.gatchigachi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import dgsw.hs.kr.gatchigachi.activity.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        

        btn_login_to_sign.setOnClickListener {
            val nextIntent = Intent(this, SignActivity::class.java)
            val animation = AnimationUtils.loadAnimation(this,R.anim.button_anim)
            btn_login_to_sign.startAnimation(animation)
            startActivity(nextIntent)
        }

        btn_login_do_login.setOnClickListener {
            val nextIntent = Intent(this, MainActivity::class.java)
            val animation = AnimationUtils.loadAnimation(this,R.anim.button_anim)
            btn_login_do_login.startAnimation(animation)
            val id : String = edit_login_id.text.toString()
            val pw : String = edit_login_pw.text.toString()

            if(check(id, pw) == 1){
                // 중복 등 데이터 확인
                if(call_server() == 1){
                    startActivity(nextIntent)
                }
            }
        }
    }
    fun call_server() : Int {
        return 1
    }



    fun check(id : String, pw : String) : Int {

        if(id.length == 0){
            Toast.makeText(this,"ID를 입력하세요", Toast.LENGTH_SHORT).show()
            edit_login_id.requestFocus();
            return -1
        }

        if(pw.length == 0){
            Toast.makeText(this,"비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            edit_login_pw.requestFocus()
            return -1
        }

        else if(pw.length < 8){
            Toast.makeText(this,"8자 이상의 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            edit_login_pw.requestFocus()
            return -1
        }

        else if(pw.length > 8){
            val password = edit_login_pw.text
            var count : Int = 0
            for(s in password){
                if( (s >= 'a' || s <= 'z') || (s >= 'A' || s <= 'Z') ){
                    count += 1
                }
                if( (s >= '0' || s <= '9')){
                    count -= 1
                }
            }
            if(count == password.length || count == -password.length){
                Toast.makeText(this, "비밀번호는 영문고 숫자의 조합이여야 합니다.", Toast.LENGTH_SHORT).show()
            }
            return -1
        }

        return 1

    }
}
