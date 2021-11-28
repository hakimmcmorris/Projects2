using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ExplosiveCrate : MonoBehaviour
{
    public float blastRadius;
    public LayerMask enemyLayer;
    public int damage;
    public float explosionForce;
    public float knockBackDistance;
    private string nameOfEffect = "knockback";



    private void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject.tag == "Bullet")
        {
            Explode();
            Destroy(this.gameObject);
        }
    }

    private void OnDrawGizmos()
    {
        Gizmos.color = Color.red;
        Gizmos.DrawWireSphere(transform.position, blastRadius);
    }


    private void Explode()
    {
        Collider2D[] enemiesWithinBlastRadius = Physics2D.OverlapCircleAll(transform.position, blastRadius, enemyLayer);

        if (enemiesWithinBlastRadius.Length >= 1)
        {
            Debug.Log("Explode");
            for (int i = 0; i < enemiesWithinBlastRadius.Length; i++)
            {
                Rigidbody2D currEnemyRB = enemiesWithinBlastRadius[i].GetComponent<Rigidbody2D>();
                enemiesWithinBlastRadius[i].GetComponent<Enemy1>().TakeDamage(damage);
                enemiesWithinBlastRadius[i].GetComponent<Enemy1>().SetEffects(true, knockBackDistance, nameOfEffect);
                KnockBack(currEnemyRB);
            }
        }
    }

    private void KnockBack(Rigidbody2D enemyRB)
    {
        Vector2 direction = enemyRB.transform.position - transform.position;
        enemyRB.AddForce(direction.normalized * explosionForce, ForceMode2D.Impulse);
    }
}
