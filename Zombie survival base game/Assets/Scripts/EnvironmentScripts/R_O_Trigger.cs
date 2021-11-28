using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class R_O_Trigger : MonoBehaviour
{
    [SerializeField] GameObject RemoveableObject;

    private void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject.CompareTag("Player"))
        {
            RemoveableObject.SetActive(false);
            Destroy(this.gameObject);
        }
    }
}
