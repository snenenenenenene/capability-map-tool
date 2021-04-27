import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import deleteImg from '../img/delete.jpg'

export default class RecentEnvironmentTableRow extends Component {

    render() {
        return (
            <tr>
                <td>
                    <Link to={ '/delete/' + this.props.obj.name }><img src={ deleteImg } alt='delete' width='15' height='18'/></Link>&nbsp;&nbsp;
                    <Link to={ '/edit/' + this.props.obj.name }>{ this.props.obj.name }</Link>
                </td>
                <td>
                    { this.props.obj.description }
                </td>
            </tr>
        )
    }
}
