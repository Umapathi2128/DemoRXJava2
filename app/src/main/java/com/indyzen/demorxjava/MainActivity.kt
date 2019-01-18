package com.indyzen.demorxjava

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rx.Observable
import rx.Subscriber
import rx.functions.Func0
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val btnName="coroutine"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnCheck.text=btnName
        btnCheck.setOnClickListener {
            selectionSwitch(btnName)

        }
    }


    private fun selectionSwitch(parm: String) {
        when (parm) {
            "from" -> {
                rxJavaObservable()
            }
            "just" -> {
                rxJavaJust()
            }
            "defer" -> {
//                rxJavaDefer()
                rxJavaDefer2()
            }
            "interval" -> {
                rxJavaInterval()
            }
            "create" -> {
                rxJavaCreate()
            }
            "single"->{rxJavaSingleObservable()}
            "coroutine"->{coroutinesDemo()}
            else -> {
            }
        }
    }

    private fun rxJavaSingleObservable() {
//        Single.create(object : Si<Movies>(){

//        })
    }

    private fun rxJavaCreate() {
        Observable.create(object : Observable.OnSubscribe<Int>{
            override fun call(t: Subscriber<in Int>?) {
                t?.onNext(1)
                t?.onNext(2)
                t?.onNext(3)
//                t?.onCompleted()
            }
        }).subscribe {
            Log.e("RxJavaCreate",":  $it")
        }
    }


    private fun rxJavaDefer2() {
        var movie = Movies("Petta")
        val movieObservable: Observable<Movies> = Observable.defer(object : Func0<Observable<Movies>> {
            override fun call(): Observable<Movies> {
                return Observable.just(movie)
            }
        })
        movie = Movies("Viswasam")
        movieObservable.subscribe {
            Log.e("RxJavaDefer2", ":  ${it.name}")
        }

    }


    /**
     * here peta only prints viswasam wont print because we started the obsevable....
     *  To over come this issue we go with defer
     *  see rxJavaDefer2 method
     */
    private fun rxJavaDefer() {
        var movie = Movies("Petta")
        val movieObservable: Observable<Movies> = Observable.just(movie)

        movie = Movies("Viswasam")
        movieObservable.subscribe(object : Subscriber<Movies>() {
            override fun onNext(t: Movies?) {
                Log.e("RxJavaDefer", "OnNext${t?.name}")
            }

            override fun onCompleted() {
                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Throwable?) {
                Log.e("RxJavaDefer", "OnError$e")
                Toast.makeText(this@MainActivity, "OnError", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun rxJavaInterval() {
        Observable.interval(1, TimeUnit.SECONDS).subscribe(object : Subscriber<Long>() {
            override fun onNext(t: Long?) {
                if (t == 10L) unsubscribe()
                Log.e("rxJavaInterval", "OnNext$t")
            }

            override fun onCompleted() {
                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Throwable?) {
                Log.e("rxJavaIntervalError", "OnError$e")
                Toast.makeText(this@MainActivity, "OnError", Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun rxJavaObservable() {

        val list = ArrayList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)
        val list2 = ArrayList<Int>()
        list2.add(10)
        list2.add(20)
        list2.add(30)
        list2.add(40)
        list2.add(50)
        Observable.from(list + 1 + "abc" + "awa" + list2).subscribe(object : Subscriber<Any>() {
            override fun onNext(t: Any?) {
                Log.e("RxJava", "OnNext$t")
            }

            override fun onCompleted() {
                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Throwable?) {
                Log.e("RxJava", "Error$e")
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun rxJavaJust() {
        /**
         *  This method for direct onNext calling....
         */
//        Observable.just(1,23).subscribe {
//            Log.e("RxJavaJust","OnNext$it")
//        }


        Observable.just(1, 2, 4, "asd", "aqwr", 6, "123").subscribe(object : Subscriber<Any>() {
            override fun onNext(t: Any?) {
                Log.e("RxJavaJust", "OnNext$t")
            }

            override fun onCompleted() {
                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Throwable?) {
                Toast.makeText(this@MainActivity, "OnError", Toast.LENGTH_LONG).show()
            }

        })
    }

    /**
     *  Coroutines demo...
     */
    private fun coroutinesDemo(){
        GlobalScope.launch {
            delay(2000)
            for(i in 0..5) {
                Log.e("Coroutines","Hello from Kotlin Coroutines!")
            }
        }
    }
}
