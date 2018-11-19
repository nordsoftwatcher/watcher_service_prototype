import React from 'react';
import styles from './NameValue.module.css';

interface NameValueProps {
  name: React.ReactNode;
  value: React.ReactNode;
  vertical?: boolean;
}

export const NameValue: React.StatelessComponent<NameValueProps> =
({ name, value, vertical }) => (
  <div className={styles.namevalue + ' ' + (vertical ? styles.vertical : '')}>
    <div className={styles.name}>
      {name}
    </div>
    <div className={styles.value}>
      {value}
    </div>
  </div>
)