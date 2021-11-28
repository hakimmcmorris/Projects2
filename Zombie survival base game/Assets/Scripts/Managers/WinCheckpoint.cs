using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
public class WinCheckpoint : MonoBehaviour
{
    // Start is called before the first frame update
    void OnTriggerEnter2D(Collider2D checktouch)
    {
        if(checktouch.CompareTag("Player"))
        {
            Debug.Log("don't touch that mouse");
            SceneManager.LoadScene("UItests");
        }
    }
}
