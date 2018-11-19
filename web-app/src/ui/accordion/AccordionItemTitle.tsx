import React from 'react';
import styles from './AccordionItemTitle.module.css';

import { FaChevronDown, FaChevronUp, FaRegCheckCircle } from 'react-icons/fa'

import { AccordionItemContext } from './AccordionItem'

export default class AccordionItemTitle extends React.Component {
  render() {
    return (
      <AccordionItemContext.Consumer>
        {({ isOpen, toggle, isSuccess }) => (
          <div className={styles.titleContainer} onClick={toggle}>
            <div className={styles.title}>
              {this.props.children}
              {' '}
              {isSuccess && <FaRegCheckCircle color='#06C575' size='15px' className={styles.successIcon}/>}
            </div>
            <div className={styles.toggleIcon}>
              {isOpen ? <FaChevronUp /> : <FaChevronDown />}
            </div>
          </div>
        )}
      </AccordionItemContext.Consumer>
    )
  }
}