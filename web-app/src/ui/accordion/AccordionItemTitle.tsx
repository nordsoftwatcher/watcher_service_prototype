import React from 'react';
import styles from './AccordionItemTitle.module.css';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { AccordionItemContext } from './AccordionItem';

export default class AccordionItemTitle extends React.Component {
  render() {
    return (
      <AccordionItemContext.Consumer>
        {({ isOpen, toggle, isSuccess }) => (
          <div className={styles.titleContainer} onClick={toggle}>
            <div className={styles.title}>
              {this.props.children}
              {' '}
              {isSuccess && <FontAwesomeIcon icon={['far', 'check-circle']} className={styles.successIcon} size='lg' />}
            </div>
            <div className={styles.toggleIcon}>
              {isOpen ? <FontAwesomeIcon icon='chevron-up' /> : <FontAwesomeIcon icon='chevron-down' />}
            </div>
          </div>
        )}
      </AccordionItemContext.Consumer>
    );
  }
}
