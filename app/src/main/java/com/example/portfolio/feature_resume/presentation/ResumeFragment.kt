package com.example.portfolio.feature_resume.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.portfolio.databinding.FragmentResumeBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
//import kotlinx.android.synthetic.main.fragment_resume.view.*


class ResumeFragment : Fragment(), View.OnClickListener {
    private var _viewBinding:FragmentResumeBinding? = null
    private val viewBinding get()= _viewBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _viewBinding = FragmentResumeBinding.inflate(inflater, container, false)
        return _viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupListeners()
        setupPDFViewer(view)
    }
    private fun setupPDFViewer(view:View){
        val pdfFileName = "kotlin.pdf"
        _viewBinding?.let{
                it.pdfView
                .fromAsset(pdfFileName)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                //.onPageChange(this)
                .enableAnnotationRendering(true)
                //.onLoad(this)
                .scrollHandle(DefaultScrollHandle(context))
                .load()
        }
    }

/* //tried webview but realized it wont work with android
    private fun displayResume(){

        val settings = resume_webview.settings
        settings.javaScriptEnabled = true
        settings.pluginState = WebSettings.PluginState.ON
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.builtInZoomControls = true
        resume_webview.webChromeClient = WebChromeClient()

//        val tester = "https://www.lsu.edu/administration/ofa/oas/pay/pdfs/f1040.pdf"//worked
        //val tester = "https://www.lsu.edu/administration/ofa/oas/pay/pdfs/f1040.pdf"//worked
        val tester = "f://android_asset/test.html"//worked

        val BASE_URL =
            //this.requireContext().assets.open("document.pdf")
            "file:///android_asset/"
            //File("${Environment.getExternalStorageDirectory()}/android_assets/test.html" )
        val data =
            "document.pdf"
          //  "document.pdf"

        val inputS = resources.assets.open("document.pdf")
        val buffer = BufferedInputStream(inputS)
        val content = String(buffer.readBytes())
        buffer.close()
        val google_doc = "https://docs.google.com/gview?embedded=true&url=" //worked

        resources.assets.open("test.html").bufferedReader().use{ br->
//            resume_webview.loadData(br.readText(), "text/html", "utf-8")
            resume_webview.loadDataWithBaseURL(google_doc, br.readText(), "text/html", "utf-8", null)
        }

        //resume_webview.loadData(content, "application/pdf", "utf-8")

        //resume_webview.loadUrl("file:///${tester2.absolutePath}")
        //resume_webview.loadUrl(BASE_URL+data)
        //resume_webview.loadDataWithBaseURL(BASE_URL, data, "text/html", "utf-8" ,null)

        //google_doc's embedded view good for opeing online pdf file.
        //val google_doc = "https://docs.google.com/gview?embedded=true&url=${tester}" //worked
        //resume_webview.loadUrl(google_doc)


        //resume_webview.loadDataWithBaseURL(google_doc, data, "application/pdf","UTF-8",null)
    }


 */
    private fun setupListeners(){
        //viewBinding?.toNextBtn?.setOnClickListener(this)
    }

    private fun setupNavigation(){
        navController = findNavController()
    }

    //clear backstack of previous fragment
    private fun clearBackStack(){
        val supportFragmentManager = activity?.supportFragmentManager
/*
        repeat(supportFragmentManager!!.backStackEntryCount){
            supportFragmentManager.popBackStack()
        }

 */
/*
        if(supportFragmentManager.backStackEntryCount >0){
            val backentry:FragmentManager.BackStackEntry =
                supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(backentry.id,FragmentManager.POP_BACK_STACK_INCLUSIVE)

        }

 */

        navController.popBackStack()
    }

/*
    override fun onClick(view: View?) {
        when(view){
            viewBinding?.toNextBtn -> {
                clearBackStack()
                val action = ResumeFragmentDirections.actionResumeFragmentToHomeFragment()
               /*
                navController.navigate(
                    R.id.homeFragment,
                    null,
                    NavOptions
                        .Builder()
                        .setPopUpTo(R.id.action_resumeFragment_to_homeFragment, true)
                        .build()
                )

                */
                navController.navigate(
                    R.id.homeFragment,
                    null,
                    NavOptions
                        .Builder()
                        .setPopUpTo(navController.graph.startDestinationId, true)
                        .build()
                )



            }
        }
    }


 */
    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    companion object {
        private const val TAG = "ResumeFragment"
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}
