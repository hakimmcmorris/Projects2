using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class BacktoMain : MonoBehaviour
{

    void Update()
    {
        if (Input.anyKey) 
        {
            Debug.Log("benji's button works");
            SceneManager.LoadScene("Main Menu");
        }
    }
}
