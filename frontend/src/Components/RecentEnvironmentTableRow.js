import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import deleteImg from '../img/delete.jpg'

export default class RecentEnvironmentTableRow extends Component {

    deleteEnvironment (){
        fetch(`http://localhost:8080/api/environment/delete/${this.props.obj.environmentId}`, {
            method: "DELETE"
        });
        this.props.history.push('http://localhost:3000/recent')
    }

    render() {
        return (
            <tr>
                <td>
                    { this.props.obj.environmentId }
                </td>

                <td>
                    <Link to={ '/environment/' + this.props.obj.environmentName }>

                    { this.props.obj.environmentName }
        </Link>
                </td>
                <td>
                    <img src={ deleteImg } onClick={() => {this.deleteEnvironment(this.props.obj.environmentId)}} alt='delete' width='15' height='18'/>
                </td>
            </tr>

        )
    }
}
