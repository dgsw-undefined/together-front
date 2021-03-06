package dgsw.hs.kr.gatchigachi.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dgsw.hs.kr.gatchigachi.model.Team
import dgsw.hs.kr.gatchigachi.model.TeamMember
import dgsw.hs.kr.gatchigachi.model.User
import kotlin.collections.ArrayList


class DBHelper(context: Context) : SQLiteOpenHelper(context, "undefined.db", null,4) {

    private val dbManager : DatabaseManager = DatabaseManager()

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("PRAGMA foreign_keys = on;")
            db.execSQL(dbManager.CreateTableAlert)
            db.execSQL(dbManager.CreateTableTeam)
            db.execSQL(dbManager.CreateTableTeamMember)
            db.execSQL(dbManager.CreateTableTruster)
            db.execSQL(dbManager.CreateTableUser)
            db.execSQL(dbManager.CreateTableToken)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if (db != null){
            db.execSQL("DROP TABLE IF EXISTS " + "alert")
            db.execSQL("DROP TABLE IF EXISTS " + "team")
            db.execSQL("DROP TABLE IF EXISTS " + "team_member")
            db.execSQL("DROP TABLE IF EXISTS " + "tec")
            db.execSQL("DROP TABLE IF EXISTS " + "truster")
            db.execSQL("DROP TABLE IF EXISTS " + "user")
            db.execSQL("DROP TABLE IF EXISTS " + "my")
            db.execSQL("DROP TABLE IF EXISTS " + "team")
        }
        onCreate(db)
    }

    fun insertUser(user:User){
//        Thread {
            val db = this.writableDatabase

            val sql = "INSERT OR REPLACE INTO user VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)"

            if(user.profile == null){
                user.profile = "http://115.68.182.229/node/profile/team/team_default.jpg"
            }

            val insertStmt = db.compileStatement(sql)

            insertStmt.clearBindings()

            insertStmt.bindLong(1, user.idx!!)
            insertStmt.bindString(2, user.id)
            insertStmt.bindString(3, user.name)
            insertStmt.bindString(4, user.pw)
            insertStmt.bindString(5, user.email)
            insertStmt.bindString(6, user.interested)
            insertStmt.bindString(7, user.github)
            insertStmt.bindString(8,user.profile)
            insertStmt.bindString(9, user.tec.toString())
            insertStmt.bindString(10, user.field)
            insertStmt.bindString(11, user.position)
            insertStmt.bindString(12, user.phone)
            insertStmt.bindLong(13,user.isMe.toLong())

            insertStmt.executeInsert()
//        }.run()
    }

    fun insertToken(token: String){
        val db = this.writableDatabase

        val sql = "INSERT OR REPLACE INTO token VALUES(?,?)"

        val insertStmt = db.compileStatement(sql)

        insertStmt.bindLong(1, 1)
        insertStmt.bindString(2, token)

        insertStmt.executeInsert()
    }

    fun insertTeams(teams: List<Team>){
        Thread {
            for (team in teams){
                val db = this.writableDatabase

                val sql = "INSERT OR REPLACE INTO team VALUES(?,?,?,?,?,?,?,?,?,?)"

                val insertStmt = db.compileStatement(sql)

                if(team.profile == null){
                    team.profile = "http://115.68.182.229/node/profile/team/team_default.jpg"
                }

                insertStmt.bindLong(1, team.id!!.toLong())
                insertStmt.bindString(2, team.name)
                insertStmt.bindString(3, team.subject)
                insertStmt.bindString(4, team.area)
                insertStmt.bindString(5, team.docs)
                insertStmt.bindLong(6, team.leader_id!!.toLong())
                insertStmt.bindString(7,team.profile)
                insertStmt.bindLong(8, team.member_limit!!.toLong())
                insertStmt.bindLong(9, team.member_count!!.toLong())
                insertStmt.bindLong(10, team.isMyTeam.toLong())

                insertStmt.executeInsert()
            }
        }.run()
    }

    fun insertTeamMembers(teamMembers : List<TeamMember>){
//        Thread {
            for (teamMember in teamMembers){
                val db = this.writableDatabase

                val sql = "INSERT OR REPLACE INTO team_member VALUES(?,?,?,?,?,?,?,?)"

                val insertStmt = db.compileStatement(sql)

                insertStmt.bindLong(1, teamMember.id!!)
                insertStmt.bindLong(2, teamMember.team_id!!)
                insertStmt.bindLong(3, teamMember.user_id!!)
                insertStmt.bindString(4,teamMember.name)
                insertStmt.bindString(5, teamMember.field)
                insertStmt.bindString(6, teamMember.enroll_date)
                insertStmt.bindLong(7, teamMember.inviter_id!!)
                insertStmt.bindLong(8, teamMember.is_leader!!)

                insertStmt.executeInsert()
            }
//        }.run()
    }

    fun selectTeamIdByUserIdx(userIdx: Int): ArrayList<Int> {
        val teamIdList = ArrayList<Int>()
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT team_id FROM team_member WHERE user_idx = $userIdx",
        null)
        while (res.moveToNext()){
            val teamId = res.getInt(res.getColumnIndex("team_id"))

            teamIdList.add(teamId)
        }

        return teamIdList
    }

    fun selectAllTeamByUserIdx(userIdx: Int):ArrayList<Team>{
        val teams = java.util.ArrayList<Team>()
        val db = this.writableDatabase

        val teamIdList = selectTeamIdByUserIdx(userIdx)

        for(teamId in teamIdList){
            val res = db.rawQuery("SELECT * FROM team WHERE id = $teamId",
                    null)

            while (res.moveToNext()) {

                val id = res.getInt(res.getColumnIndex("id"))
                val name = res.getString(res.getColumnIndex("name"))
                val subject = res.getString(res.getColumnIndex("subject"))
                val area = res.getString(res.getColumnIndex("area"))
                val docs = res.getString(res.getColumnIndex("docs"))
                val leaderId = res.getInt(res.getColumnIndex("leader_id"))
                val profile = res.getString(res.getColumnIndex("profile"))
                val memberLimit = res.getInt(res.getColumnIndex("member_limit"))
                val memberCount = res.getInt(res.getColumnIndex("member_count"))

                val teamTemp = Team(id,name,subject,area,docs,leaderId,profile,memberLimit,memberCount)

                teams.add(teamTemp)
            }
            res.close()
        }

        return teams
    }

    fun selectAllMyTeam():ArrayList<Team>{
        val teams = java.util.ArrayList<Team>()
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT * FROM team WHERE is_my_team = 1;",
                null)

        while (res.moveToNext()) {

            val id = res.getInt(res.getColumnIndex("id"))
            val name = res.getString(res.getColumnIndex("name"))
            val subject = res.getString(res.getColumnIndex("subject"))
            val area = res.getString(res.getColumnIndex("area"))
            val docs = res.getString(res.getColumnIndex("docs"))
            val leaderId = res.getInt(res.getColumnIndex("leader_id"))
            val profile = res.getString(res.getColumnIndex("profile"))
            val memberLimit = res.getInt(res.getColumnIndex("member_limit"))
            val memberCount = res.getInt(res.getColumnIndex("member_count"))

            val teamTemp = Team(id,name,subject,area,docs,leaderId,profile,memberLimit,memberCount)

            teamTemp.isMyTeam = 1

            teams.add(teamTemp)
        }

        res.close()

        return teams
    }

    fun selectAllTeam():ArrayList<Team>{
        val teams = java.util.ArrayList<Team>()
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT * FROM team;",
                null)

        while (res.moveToNext()) {

            val id = res.getInt(res.getColumnIndex("id"))
            val name = res.getString(res.getColumnIndex("name"))
            val subject = res.getString(res.getColumnIndex("subject"))
            val area = res.getString(res.getColumnIndex("area"))
            val docs = res.getString(res.getColumnIndex("docs"))
            val leaderId = res.getInt(res.getColumnIndex("leader_id"))
            val profile = res.getString(res.getColumnIndex("profile"))
            val memberLimit = res.getInt(res.getColumnIndex("member_limit"))
            val memberCount = res.getInt(res.getColumnIndex("member_count"))

            val teamTemp = Team(id,name,subject,area,docs,leaderId,profile,memberLimit,memberCount)

            teams.add(teamTemp)
        }

        res.close()

        return teams
    }

    fun deleteMyTeam(){
        val db = this.writableDatabase

        val res = db.rawQuery("DELETE FROM team", null)

        res.moveToNext()


        res.close()

    }

    fun deleteMyInfo(){
        val db = this.writableDatabase

        val res = db.rawQuery("DELETE FROM user", null)

        res.moveToNext()

        res.close()

        deleteMyTeam()
    }

    fun deleteToken(){
        val db = this.writableDatabase

        val res = db.rawQuery("DELETE FROM token", null)

        res.moveToNext()

        res.close()

    }

    fun selectMyInfo() : User? {
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT * FROM user WHERE is_me =  1",
                null)

        while (res.moveToNext()){

            val idx = res.getLong(res.getColumnIndex("idx"))
            val id = res.getString(res.getColumnIndex("id"))
            val name = res.getString(res.getColumnIndex("name"))
            val pw = res.getString(res.getColumnIndex("pw"))
            val email = res.getString(res.getColumnIndex("email"))
            val interested = res.getString(res.getColumnIndex("interested"))
            val profile = res.getString(res.getColumnIndex("profile"))
            val github = res.getString(res.getColumnIndex("github"))
            val tec = res.getString(res.getColumnIndex("tec"))
            val field = res.getString(res.getColumnIndex("field"))
            val position = res.getString(res.getColumnIndex("position"))
            val phone = res.getString(res.getColumnIndex("phone"))

            res.close()

            val tecArray = tec.split(" ".toRegex())

            return User(idx,id,name,pw,email,interested,github,profile,field,tecArray,position,phone)
        }
        return null
    }

    fun selectUserById(idx:Int) : User? {
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT * FROM user WHERE idx =  $idx",
                null)

        while (res.moveToNext()){

            val idx = res.getLong(res.getColumnIndex("idx"))
            val id = res.getString(res.getColumnIndex("id"))
            val name = res.getString(res.getColumnIndex("name"))
            val pw = res.getString(res.getColumnIndex("pw"))
            val email = res.getString(res.getColumnIndex("email"))
            val interested = res.getString(res.getColumnIndex("interested"))
            val github = res.getString(res.getColumnIndex("github"))
            val tec = res.getString(res.getColumnIndex("tec"))
            val field = res.getString(res.getColumnIndex("field"))
            val position = res.getString(res.getColumnIndex("position"))
            val phone = res.getString(res.getColumnIndex("phone"))
            val profile = res.getString(res.getColumnIndex("profile"))

            res.close()

            val tecArray = tec.split(" ".toRegex())

            return User(idx,id,name,pw,email,interested,github,profile,field,tecArray,position,phone)
        }

        return null
    }

    fun selectAllUser() : ArrayList<User> {
        val users = ArrayList<User>()
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT * FROM user;",
                null)

        while (res.moveToNext()){

            val idx = res.getLong(res.getColumnIndex("idx"))
            val id = res.getString(res.getColumnIndex("id"))
            val name = res.getString(res.getColumnIndex("name"))
            val pw = res.getString(res.getColumnIndex("pw"))
            val email = res.getString(res.getColumnIndex("email"))
            val interested = res.getString(res.getColumnIndex("interested"))
            val github = res.getString(res.getColumnIndex("github"))
            val tec = res.getString(res.getColumnIndex("tec"))
            val field = res.getString(res.getColumnIndex("field"))
            val position = res.getString(res.getColumnIndex("position"))
            val phone = res.getString(res.getColumnIndex("phone"))
            val profile = res.getString(res.getColumnIndex("profile"))


            val tecArray = tec.split(" ".toRegex())

            users.add(User(idx,id,name,pw,email,interested,github,profile,field,tecArray,position,phone))
        }

        res.close()

        return users
    }

    fun selectToken():String{
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT token FROM token ORDER BY idx DESC LIMIT 1",
                null)

        val buffer = StringBuffer()

        while (res.moveToNext()) {
            buffer.append(res.getString(0))
        }

        val token = buffer.toString()

        res.close()

        return token

    }

    fun selectTeamById(teamId:Int):Team?{
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT * FROM team WHERE id = $teamId",
                null)

        while (res.moveToNext()) {

            val id = res.getInt(res.getColumnIndex("id"))
            val name = res.getString(res.getColumnIndex("name"))
            val subject = res.getString(res.getColumnIndex("subject"))
            val area = res.getString(res.getColumnIndex("area"))
            val docs = res.getString(res.getColumnIndex("docs"))
            val leaderId = res.getInt(res.getColumnIndex("leader_id"))
            val profile = res.getString(res.getColumnIndex("profile"))
            val memberLimit = res.getInt(res.getColumnIndex("member_limit"))
            val memberCount = res.getInt(res.getColumnIndex("member_count"))
            val isMyTeam = res.getInt(res.getColumnIndex("is_my_team"))

            res.close()

            val team = Team(id,name,subject,area,docs,leaderId,profile,memberLimit,memberCount)

            team
            team.isMyTeam = isMyTeam

            return team

        }

        res.close()

        return null
    }

    fun selectTeamMembersByTeamId(teamId: Int):ArrayList<TeamMember>{
        val teamMembers = java.util.ArrayList<TeamMember>()
        val db = this.writableDatabase

        val res = db.rawQuery("SELECT * FROM team_member WHERE team_id = $teamId;",
                null)

        while (res.moveToNext()) {

            val id = res.getLong(res.getColumnIndex("id"))
            val teamId = res.getLong(res.getColumnIndex("team_id"))
            val userId = res.getLong(res.getColumnIndex("user_idx"))
            val name = res.getString(res.getColumnIndex("name"))
            val field = res.getString(res.getColumnIndex("field"))
            val enroll_date = res.getString(res.getColumnIndex("enroll_date"))
            val inviterId = res.getLong(res.getColumnIndex("inviter_id"))
            val isLeader = res.getLong(res.getColumnIndex("is_leader"))

            val teamTemp = TeamMember(id,teamId,userId,name,field,inviterId,enroll_date,null,null,isLeader)

            teamMembers.add(teamTemp)
        }

        res.close()


        return teamMembers
    }

}