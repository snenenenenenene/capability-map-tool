import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import deleteImg from '../img/delete.jpg'
import axios from 'axios';

export default class RecentEnvironmentTableRow extends Component {

    constructor(props) {
        super(props);
        this.state = {
        };
    }

    deleteEnvironment (){
        console.log(this.props.environmentId)
        axios.delete(`${process.env.REACT_APP_API_URL}/environment/delete/${this.props.obj.environmentId}`)
        .then(response => console.log(response))
        .catch(error => console.log(error))
        this.props.history.push('/recent')
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
