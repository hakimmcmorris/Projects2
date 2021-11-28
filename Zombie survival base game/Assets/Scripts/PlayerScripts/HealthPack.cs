using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HealthPack : MonoBehaviour
{
    #region Healing_variables
    public float healing_value;
    [Tooltip("How much it heals the player")]
    #endregion
    private Vector3 StartingPos;


    #region HealingFunction
    public virtual void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.transform.CompareTag("Player"))
        {
            collision.GetComponent<Player>().TakeDamage(-healing_value);
            
            Destroy(this.gameObject);
        }
    }
    #endregion
}
