package com.example.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.chat.objs.EmailWithMessages
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.TransportEnum
import io.reactivex.rxjava3.core.Single
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var connection: HubConnection
        val emails = MutableLiveData<MutableList<EmailWithMessages>>()
        const val BASE_URL = "https://192.168.255.144:7175"
        var JWT = MutableLiveData<String>()
        var email = ""
        var userName = ""
        private fun trustAllCertificates() {
            try {
                val trustAllCerts: Array<TrustManager> = arrayOf(
                    object : X509TrustManager {
                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }

                        override fun checkClientTrusted(
                            certs: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            certs: Array<X509Certificate>,
                            authType: String
                        ) {
                        }
                    }
                )
                val sc = SSLContext.getInstance("SSL")
                sc.init(null, trustAllCerts, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
                HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
            } catch (ignored: Exception) {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trustAllCertificates()
        JWT.observe(this) { jwt ->
            if (jwt != "") {
                connection = HubConnectionBuilder
                    .create("$BASE_URL/chat/")
                    .withAccessTokenProvider(Single.defer { Single.just(jwt) })
                    .withTransport(TransportEnum.WEBSOCKETS)
                    .build()
//                connection.on("ReceiveMessage", { senderEmail, message ->
//                    emails.value?.find { it.email == senderEmail }.apply {
//                        if (this != null) {
//                            this.messages.value?.add(message)
//                        } else {
//                            emails.value?.add(
//                                // TODO: Assuming these are all the messages send to current user
//                                EmailWithMessages(
//                                    senderEmail,
//                                    MutableLiveData(mutableListOf(message))
//                                )
//                            )
//                        }
//                    }
//                }, String::class.java, String::class.java)
                connection.start()
            }
        }
    }

    public override fun onStart() {
        super.onStart()

    }
}