using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Bullet : MonoBehaviour
{
    #region Damage_variables
    public float damage;
    [Tooltip("Must be at least 1 to kill an enemy")]
    public int piercing;
    #endregion
    public Vector3 StartingPos;

    #region Unity_funcs
    private void Awake()
    {
        StartingPos = transform.position;
    }

    public virtual void Update()
    {
        if(Vector3.Distance(StartingPos, transform.position)> 10f)
        {
            Debug.Log(StartingPos);
            Debug.Log(transform.position);
            Destroy(this.gameObject);
        }
    }
    #endregion

    #region Damag_funcs
    public virtual void OnTriggerEnter2D(Collider2D collision)
    {
        Debug.Log("Collision: asdf " + collision.gameObject.tag);
        if (collision.transform.CompareTag("Enemy"))
        {
            collision.GetComponent<Enemy1>().TakeDamage(damage);
            
        }
        if (collision.transform.CompareTag("DestructableTile")){
            
            collision.GetComponent<DestructableTile>().destruct(this.gameObject.transform, GetComponent<Rigidbody2D>().velocity);
        }
        piercing--;
        if(piercing <= 0)
        {
            Destroy(this.gameObject);
        }
    }
    #endregion
}
