package com.deeosoft.daggerprojects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deeosoft.daggerprojects.common.Constants
import com.deeosoft.daggerprojects.model.Question
import com.deeosoft.daggerprojects.networking.StackOverFlowApiService
import kotlinx.android.synthetic.main.question_list_screen.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionListActivity: AppCompatActivity() {
    private lateinit var questionsAdapter: QuestionListAdapter
    private lateinit var stackOverFlowApi: StackOverFlowApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_list_screen)

        swipeRefresh.setOnRefreshListener {
            fetchQuestions()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        questionsAdapter = QuestionListAdapter { question ->
            println("go to the question details screen passing the id ${question.id}")
        }
        recycler.adapter = questionsAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        stackOverFlowApi = retrofit.create(StackOverFlowApiService::class.java)
    }

    private fun fetchQuestions(){
        GlobalScope.launch {
            println("show loader")
            try {
                val response = stackOverFlowApi.lastActiveQuestions(20)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { questionsAdapter.bindData(it.questions) }
                }
            }catch (t: Throwable){
                if (t is CancellationException){
                    println("failed for some reasons")
                }
            }finally {
                println("remove loader")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fetchQuestions()
    }
}

class QuestionListAdapter(private val onQuestionClickListener: (Question) -> Unit): RecyclerView.Adapter<QuestionListAdapter.QuestionListViewHolder>(){
    private var questions: List<Question> = listOf()

    fun bindData(questions: List<Question>){
        this.questions = questions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.question_list_item_view, parent, false)
        return QuestionListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionListViewHolder, position: Int) {
        holder.question.text = questions[position].title
    }

    override fun getItemCount(): Int {
        return questions.size
    }


    inner class QuestionListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var question: TextView = itemView.findViewById(R.id.txt_title)
    }
}